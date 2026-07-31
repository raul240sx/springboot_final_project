package rrs.ms_sales.dto;

import java.math.BigDecimal;
import java.util.List;


public record SaleMajorAmountDTO(
    String saleCode,
    BigDecimal totalAmount,
    String clientName,
    String clientLastName,
    String clientCode,
    List<DetailResponseDTO> details
) {

}
