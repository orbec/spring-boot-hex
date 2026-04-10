package com.bac.microservice.investmentfunds.rest.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.util.List;

@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "Generic response DTO that wraps data and error information")
public record ResponseDto<T>(
    @Schema(description = "The response data", nullable = true, anyOf = {})
    T data,

    @Schema(description = "List of errors associated with the response, if any", nullable = true)
    List<ErrorDto> errors
) {

}
