package com.bac.microservice.investmentfunds.rest.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.util.Map;

@Builder(toBuilder = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "Error details, including error code and description")
public record ErrorDto(
    @Schema(description = "Error code identifying the type of error", example = "MSIF_GE_00001}")
    String code,

    @Schema(description = "Description of the error", example = "Invalid request parameter")
    String description,

    @Schema(description = "Attributes for describe fields with invalid values", example = "{\"field\": \"message\"}")
    Map<String, String> attributes
) {
}
