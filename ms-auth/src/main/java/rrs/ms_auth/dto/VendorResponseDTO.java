package rrs.ms_auth.dto;

import java.util.List;

public record VendorResponseDTO(
    String code,
    String name,
    String lastName,
    String dni,
    List<String> roles
) {

}
