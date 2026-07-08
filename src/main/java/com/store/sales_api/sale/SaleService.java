package com.store.sales_api.sale;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.store.sales_api.client.Client;
import com.store.sales_api.client.IClientRepository;
import com.store.sales_api.common.exception.BusinessRuleException;
import com.store.sales_api.common.exception.ResourceNotFoundException;
import com.store.sales_api.product.IProductRepository;
import com.store.sales_api.product.Product;
import com.store.sales_api.sale.dto.DetailRequestDTO;
import com.store.sales_api.sale.dto.SaleMajorAmountDTO;
import com.store.sales_api.sale.dto.SaleRequestDTO;
import com.store.sales_api.sale.dto.SaleResponseDTO;
import com.store.sales_api.sale.dto.SaleSumCountDTO;
import com.store.sales_api.util.SaleCodeGenerator;


@Service
@Transactional(readOnly = true)
public class SaleService implements ISaleService{
    private final ISaleRepository saleRepository;
    private final IClientRepository clientRepository;
    private final IProductRepository productRepository;
    private final SaleCodeGenerator generator;
    private final SaleDTOMapper dtoMapper;

    public SaleService(ISaleRepository saleRepository, IClientRepository clientRepository, IProductRepository productRepository, SaleCodeGenerator generator, SaleDTOMapper dtoMapper){
        this.saleRepository = saleRepository;
        this.clientRepository = clientRepository;
        this.productRepository = productRepository;
        this.generator = generator;
        this.dtoMapper = dtoMapper;

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
        //Busqueda del cliente y productos requeridos
        Client client = clientRepository.findByCode(saleDTO.clientCode()).orElseThrow(() -> new ResourceNotFoundException("El cliente seleccionado para la venta no existe"));
        List<String> productCodes = saleDTO.details().stream().map(detailDTO -> detailDTO.productCode()).toList();
        List<Product> products = productRepository.findAllByCodeIn(productCodes);
        Map<String, Product> productsMap = products.stream().collect(Collectors.toMap(product -> product.getCode(), product -> product));

        //Validación de productos y stock
        for (DetailRequestDTO detail : saleDTO.details()) {
            if (productsMap.get(detail.productCode()) == null) {
                throw new ResourceNotFoundException("El producto con código " + detail.productCode() + " no existe en los registros");
            }
            if (detail.quantity() > productsMap.get(detail.productCode()).getStock()) {
                throw new BusinessRuleException("La cantidad deseada del producto " + detail.productCode() + " supera el stock. Maximo " + productsMap.get(detail.productCode()).getStock() +" productos");
            }
        }

        //Creación de la venta y sus detalles
        Sale newSale = new Sale();
        newSale.setClient(client);
        newSale.setDate(LocalDate.now());

        BigDecimal totalAmount = new BigDecimal(0);
        for (DetailRequestDTO detailDTO : saleDTO.details()) {

            Detail newDetail = new Detail();
            Product selectedProduct = productsMap.get(detailDTO.productCode());
            newDetail.setProduct(selectedProduct);
            newDetail.setQuantity(detailDTO.quantity());
            newDetail.setPartialAmount(selectedProduct.getPrice().multiply(BigDecimal.valueOf(detailDTO.quantity())));
            
            selectedProduct.setStock(selectedProduct.getStock() - detailDTO.quantity());
            newSale.addDetail(newDetail);
            totalAmount = totalAmount.add(newDetail.getPartialAmount());
        }

        newSale.setTotalAmount(totalAmount);

        Sale createdSale = saleRepository.save(newSale);

        //Generación y asignación del código de la venta
        createdSale.setCode(generator.generateSaleCode(createdSale.getDate(), createdSale.getId()));
        
        return dtoMapper.saleToDTO(createdSale);       
    }

    @Override
    @Transactional
    public SaleResponseDTO updateSale(String saleCode, SaleRequestDTO saleDTO) {
        //Busqueda de la venta a editar y del cliente
        Sale saleToUpdate = saleRepository.findByCode(saleCode).orElseThrow(() -> new ResourceNotFoundException("La venta a editar no existe"));
        Client clientToUpdate = clientRepository.findByCode(saleDTO.clientCode()).orElseThrow(() -> new ResourceNotFoundException("El cliente seleccionado para la venta no existe"));

        //Volver los productos al estado anterior a la venta
        saleToUpdate.getDetails().stream().map(detail -> {
            Product product = detail.getProduct();
            product.setStock(product.getStock() + detail.getQuantity());
            return product;
        }).toList();

        //Vaciar la lista de detalles de la venta
        saleToUpdate.getDetails().clear();

        //Verificar si es necesario reemplazar el cliente
        Client lastClient = saleToUpdate.getClient();
        if (lastClient != clientToUpdate) {
            saleToUpdate.setClient(clientToUpdate);
        }

        //Busqueda de los productos en el nuevo detalle de compra
        List<String> productCodesEdit = saleDTO.details().stream().map(detail -> detail.productCode()).toList();
        List<Product> foundProducts = productRepository.findAllByCodeIn(productCodesEdit);
        Map<String, Product> productsMap = foundProducts.stream().collect(Collectors.toMap(product -> product.getCode(), product -> product));
        
        //Validación de existencia de productos y de stock
        for (DetailRequestDTO detail : saleDTO.details()) {
            Product selectedProduct = productsMap.get(detail.productCode());
            if (selectedProduct == null) {
                throw new ResourceNotFoundException("El producto con código " + detail.productCode() + " no existe en los registros");
            }
            if (detail.quantity() > selectedProduct.getStock()) {
                throw new BusinessRuleException("La cantidad deseada del producto " + detail.productCode() + " supera el stock. Maximo " + selectedProduct.getStock() +" productos");
            }
        }

        //Creacion de nuevos detalles de la venta
        BigDecimal totalAmount = new BigDecimal(0);
        for (DetailRequestDTO detail : saleDTO.details()) {

            Product selectedProduct = productsMap.get(detail.productCode());
            Detail newDetail = new Detail();
            newDetail.setProduct(selectedProduct);
            newDetail.setSale(saleToUpdate);
            newDetail.setQuantity(detail.quantity());
            newDetail.setPartialAmount(selectedProduct.getPrice().multiply(BigDecimal.valueOf(detail.quantity())));
            
            saleToUpdate.addDetail(newDetail);
            selectedProduct.setStock(selectedProduct.getStock() - detail.quantity());
            totalAmount = totalAmount.add(newDetail.getPartialAmount());
        }

        saleToUpdate.setTotalAmount(totalAmount);

        return dtoMapper.saleToDTO(saleToUpdate);
    }

    @Override
    @Transactional
    public void deleteSale(String saleCode) {
        Sale saleToDelete = saleRepository.findByCode(saleCode).orElseThrow(() -> new ResourceNotFoundException("La venta a ser eliminada no existe"));

        for (Detail detail : saleToDelete.getDetails()) {

            Product product = detail.getProduct();
            product.setStock(product.getStock() + detail.getQuantity());
        }

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

        Integer productQuantity = 0;

        for (Detail detail : sale.getDetails()) {
            productQuantity += detail.getQuantity();
        }

        return dtoMapper.saleMajorAmountToDTO(sale, productQuantity);
    }
    

}
