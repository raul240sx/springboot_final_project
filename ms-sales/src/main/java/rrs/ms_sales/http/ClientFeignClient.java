package rrs.ms_sales.http;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import rrs.ms_sales.dto.ClientDTO;


@FeignClient(name = "ms-clients")
public interface ClientFeignClient {

    @GetMapping("/api/clients/{clientCode}")
    public ClientDTO getClient(@PathVariable("clientCode") String clientCode);
}
