package rrs.ms_sales.http;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import rrs.ms_sales.dto.DetailRequestDTO;
import rrs.ms_sales.dto.DetailResponseDTO;


@FeignClient(name = "ms-products")
public interface ProductFeignClient{

    @PostMapping("/api/products/validate-products-reduce-stock")
    public List<DetailResponseDTO> validateReduceStock(@RequestBody List<DetailRequestDTO> dto);

    @PostMapping("/api/products/return-stock")
    public void returnStock(@RequestBody List<DetailRequestDTO> dto);

}
