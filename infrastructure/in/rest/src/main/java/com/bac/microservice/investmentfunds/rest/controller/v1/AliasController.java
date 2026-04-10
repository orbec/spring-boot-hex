package com.bac.microservice.investmentfunds.rest.controller.v1;


import com.bac.microservice.investmentfunds.rest.dto.AliasRequestDto;
import com.bac.microservice.investmentfunds.rest.dto.AliasResponseDto;
import com.bac.utils.openapi.OpenApiDoc;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import reactor.core.publisher.Mono;

@Tag(name = "Alias controller",
        description = "This controller exposes methods to manage the alias of an investment fund")

public interface AliasController {

    @Operation(summary = "Alias for investment fund", description = "Insert or update alias for a given investment fund")
    @ApiResponses(value = {
        @ApiResponse(responseCode = OpenApiDoc.OK_CODE,
                description = OpenApiDoc.OK_LABEL),
        @ApiResponse(responseCode = OpenApiDoc.INTERNAL_SERVER_ERROR_CODE,
                description = OpenApiDoc.INTERNAL_SERVER_ERROR_LABEL),
        @ApiResponse(responseCode = "403",
                description = "Forbidden")
    })
    Mono<ResponseEntity<AliasResponseDto>> insertOrUpdateInvestmentFundAlias(
            @PathVariable("number") @NotNull @NotBlank String number,
            @RequestParam("country") @NotNull @NotBlank String country,
            @RequestBody @Valid AliasRequestDto alias,
            @Parameter(description = "Procedencia: IOs, Android")
            @RequestHeader("channel") String channel,
            ServerHttpRequest serverHttpRequest
    );

    @Operation(summary = "Alias for investment fund", description = "Insert or update alias for a given investment fund")
    @ApiResponses(value = {
        @ApiResponse(responseCode = OpenApiDoc.OK_CODE,
                description = OpenApiDoc.OK_LABEL),
        @ApiResponse(responseCode = OpenApiDoc.INTERNAL_SERVER_ERROR_CODE,
                description = OpenApiDoc.INTERNAL_SERVER_ERROR_LABEL),
        @ApiResponse(responseCode = "403",
                description = "Forbidden")
    })
    Mono<ResponseEntity<AliasResponseDto>> insertOrUpdateInvestmentFundAliasVersion(
            @PathVariable("number") @NotNull @NotBlank String number,
            @RequestParam("country") @NotNull @NotBlank String country,
            @RequestBody @Valid AliasRequestDto alias,
            @Parameter(description = "Procedencia: IOs, Android")
            @RequestHeader("channel") String channel,
            ServerHttpRequest serverHttpRequest
    );

    @Operation(summary = "Alias for investment fund", description = "Insert or update alias for a given investment fund")
    @ApiResponses(value = {
        @ApiResponse(responseCode = OpenApiDoc.OK_CODE,
                description = OpenApiDoc.OK_LABEL),
        @ApiResponse(responseCode = OpenApiDoc.INTERNAL_SERVER_ERROR_CODE,
                description = OpenApiDoc.INTERNAL_SERVER_ERROR_LABEL),
        @ApiResponse(responseCode = "403",
                description = "Forbidden")
    })
    Mono<ResponseEntity<AliasResponseDto>> insertOrUpdateInvestmentFundAliasVersionThree(
            @PathVariable("number") @NotNull @NotBlank String number,
            @RequestParam("country") @NotNull @NotBlank String country,
            @RequestBody @Valid AliasRequestDto alias,
            @Parameter(description = "Procedencia: IOs, Android")
            @RequestHeader("channel") String channel,
            ServerHttpRequest serverHttpRequest
    );
}
