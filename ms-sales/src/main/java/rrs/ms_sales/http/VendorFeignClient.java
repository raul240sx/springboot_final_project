package rrs.ms_sales.http;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import rrs.ms_sales.dto.VendorDTO;


@FeignClient(name = "ms-auth")
public interface VendorFeignClient {

    @GetMapping("/api/vendors/{vendorCode}")
    public VendorDTO getVendor(@PathVariable String vendorCode);

}
