package rrs.ms_sales.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import rrs.ms_sales.common.DTOMapper;
import rrs.ms_sales.common.SaleCodeGenerator;
import rrs.ms_sales.common.exception.BusinessRuleException;
import rrs.ms_sales.common.exception.ResourceNotFoundException;
import rrs.ms_sales.common.exception.SaleProcessingException;
import rrs.ms_sales.dto.ClientDTO;
import rrs.ms_sales.dto.DetailRequestDTO;
import rrs.ms_sales.dto.DetailResponseDTO;
import rrs.ms_sales.dto.SaleMajorAmountDTO;
import rrs.ms_sales.dto.SaleRequestDTO;
import rrs.ms_sales.dto.SaleResponseDTO;
import rrs.ms_sales.dto.SaleSumCountDTO;
import rrs.ms_sales.dto.VendorDTO;
import rrs.ms_sales.http.ClientFeignClient;
import rrs.ms_sales.http.ProductFeignClient;
import rrs.ms_sales.http.VendorFeignClient;
import rrs.ms_sales.model.Detail;
import rrs.ms_sales.model.Sale;
import rrs.ms_sales.repository.ISaleRepository;



@Service
@Transactional(readOnly = true)
public class SaleService implements ISaleService{
    private final ISaleRepository saleRepository;
    private final ProductFeignClient productFeignClient;
    private final ClientFeignClient clientFeignClient;
    private final VendorFeignClient vendorFeignClient;
    private final SaleCodeGenerator generator;
    private final DTOMapper dtoMapper;

    public SaleService(ISaleRepository saleRepository, ProductFeignClient productFeignClient, ClientFeignClient clientFeignClient, VendorFeignClient vendorFeignClient, SaleCodeGenerator generator, DTOMapper dtoMapper){
        this.saleRepository = saleRepository;
        this.productFeignClient = productFeignClient;
        this.clientFeignClient = clientFeignClient;
        this.vendorFeignClient = vendorFeignClient;
        this.generator = generator;
        this.dtoMapper = dtoMapper;

    }


    private String getRequestUserCode() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }

    private Boolean isCurrentUserAdmin() {
        return SecurityContextHolder.getContext().getAuthentication().getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
    }



    @Override
    public List<SaleResponseDTO> getSales() {
        return saleRepository.findAll().stream().map(sale -> dtoMapper.saleToDTO(sale)).toList();
    }

    @Override
    public SaleResponseDTO getSale(String saleCode) {
        Sale sale = saleRepository.findByCode(saleCode).orElseThrow(() -> new ResourceNotFoundException("La venta buscada no existe"));
        
        return dtoMapper.saleToDTO(sale);
    }

    @Override
    @Transactional
    public SaleResponseDTO createSale(SaleRequestDTO saleDTO) {
        //Busqueda del cliente, vendor y productos requeridos
        ClientDTO client = clientFeignClient.getClient(saleDTO.clientCode());
        VendorDTO vendor = vendorFeignClient.getVendor(this.getRequestUserCode());
        List<DetailResponseDTO> checkedProducts = productFeignClient.validateReduceStock(saleDTO.details());

        try {
            //Creación de la venta y sus detalles
            Sale newSale = new Sale();
            newSale.setClientCode(client.code());
            newSale.setVendorCode(vendor.code());
            newSale.setDate(LocalDate.now());

            BigDecimal totalAmount = new BigDecimal(0);
            for (DetailResponseDTO checkedProduct : checkedProducts) {

                Detail newDetail = new Detail();
                newDetail.setProductCode(checkedProduct.productCode());
                newDetail.setQuantity(checkedProduct.quantity());
                newDetail.setPartialAmount(checkedProduct.partialAmount());
                
                newSale.addDetail(newDetail);
                totalAmount = totalAmount.add(newDetail.getPartialAmount());
            }

            newSale.setTotalAmount(totalAmount);
            Sale createdSale = saleRepository.save(newSale);

            //Generación y asignación del código de la venta
            createdSale.setCode(generator.generateSaleCode(createdSale.getDate(), createdSale.getId()));
            saleRepository.saveAndFlush(createdSale);
            
            return dtoMapper.saleToDTO(createdSale);
            
        } catch (Exception e) {
            productFeignClient.returnStock(saleDTO.details());
            throw new SaleProcessingException("Hubo un problema al realizar la operación, intente mas tarde.", e);
        }
    }

    @Override
    @Transactional
    public SaleResponseDTO updateSale(String saleCode, SaleRequestDTO saleDTO) {
        //Busqueda de la venta a editar
        Sale saleToUpdate = saleRepository.findByCode(saleCode).orElseThrow(() -> new ResourceNotFoundException("La venta a editar no existe"));

        //Comprobación de vendedor
        String currentVendorCode = this.getRequestUserCode();
        if (!currentVendorCode.equals(saleToUpdate.getVendorCode()) && !isCurrentUserAdmin()) {
            throw new BusinessRuleException("No es posible modificar la venta de otro vendedor");
        }
        vendorFeignClient.getVendor(currentVendorCode);

        //Busqueda del cliente
        ClientDTO clientToUpdate = clientFeignClient.getClient(saleDTO.clientCode());

        //Volver los productos al estado anterior a la venta
        List<DetailRequestDTO> details = saleToUpdate.getDetails().stream().map(detail -> dtoMapper.detailRequestToDTO(detail)).toList();
        productFeignClient.returnStock(details);

        //Busqueda de los productos en el nuevo detalle de compra
        List<DetailRequestDTO> productCodesEdit = saleDTO.details();
        List<DetailResponseDTO> foundProducts;
        try {            
            foundProducts = productFeignClient.validateReduceStock(productCodesEdit);
        } catch (Exception e) {
            productFeignClient.validateReduceStock(details);
            throw new SaleProcessingException("Ocurrió un error al consultar los productos", e);
        }
        

        try {
            //Vaciar la lista de detalles de la venta
            saleToUpdate.getDetails().clear();

            //Verificar si es necesario reemplazar el cliente
            String lastClientCode = saleToUpdate.getClientCode();
            if (!lastClientCode.equals(clientToUpdate.code())) {
                saleToUpdate.setClientCode(clientToUpdate.code());
            }

            //Creacion de nuevos detalles de la venta
            BigDecimal totalAmount = new BigDecimal(0);
            for (DetailResponseDTO product : foundProducts) {
                Detail newDetail = new Detail();
                newDetail.setProductCode(product.productCode());
                newDetail.setQuantity(product.quantity());
                newDetail.setPartialAmount(product.partialAmount());
                newDetail.setSale(saleToUpdate);
                
                saleToUpdate.addDetail(newDetail);
                totalAmount = totalAmount.add(product.partialAmount());
            }

            saleToUpdate.setTotalAmount(totalAmount);
            saleRepository.saveAndFlush(saleToUpdate);
            
            return dtoMapper.saleToDTO(saleToUpdate);
            
        } catch (Exception e) {
            productFeignClient.returnStock(productCodesEdit);
            throw new SaleProcessingException("Hubo un problema al realizar la operación, intente mas tarde.", e);
        }
    }

    @Override
    @Transactional
    public void deleteSale(String saleCode) {        
        Sale saleToDelete = saleRepository.findByCode(saleCode).orElseThrow(() -> new ResourceNotFoundException("La venta a ser eliminada no existe"));
        List<DetailRequestDTO> details = saleToDelete.getDetails().stream().map(detail -> dtoMapper.detailRequestToDTO(detail)).toList();
        productFeignClient.returnStock(details);
        saleRepository.delete(saleToDelete);
    }



    @Override
    public SaleSumCountDTO getDaySaleSumCount(LocalDate date) {
        Integer salesCount = saleRepository.getDaySalesCount(date);

        BigDecimal dayTotalAmount = saleRepository.getDayTotalAmount(date);

        return dtoMapper.saleSumCounToDTO(date, salesCount, dayTotalAmount);
    }



    @Override
    public SaleMajorAmountDTO getBestSale() {
        Sale sale = saleRepository.findTopByOrderByTotalAmountDesc().orElseThrow(() -> new ResourceNotFoundException("No existe registros para mostrar"));
        ClientDTO client = clientFeignClient.getClient(sale.getClientCode());        

        return dtoMapper.saleMajorAmountToDTO(sale, client);
    }
}
