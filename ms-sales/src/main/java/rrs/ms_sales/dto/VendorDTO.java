package rrs.ms_sales.dto;

import java.util.List;

public record VendorDTO(
    String code,
    String name,
    String lastName,
    String dni,
    List<String> roles
) {

}
