package rrs.ms_auth.dto;

import jakarta.validation.constraints.NotBlank;

public record RoleRequestDTO(
    @NotBlank(message = "{role.name.null}")
    String name
) {

}
