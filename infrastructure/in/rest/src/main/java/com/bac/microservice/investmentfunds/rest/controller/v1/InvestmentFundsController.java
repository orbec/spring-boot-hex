package com.bac.microservice.investmentfunds.rest.controller.v1;

import com.bac.microservice.investmentfunds.model.investmentfunds.InvestmentFundsDto;
import com.bac.microservice.investmentfunds.model.investmentfunds.MetaDataInvestmentFundsDto;
import com.bac.utils.openapi.OpenApiDoc;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.bind.annotation.RequestHeader;
import reactor.core.publisher.Mono;

@Tag(name = "InvestmentFunds controller", description = "This is the description of the service InvestmentFunds")
public interface InvestmentFundsController {

    @Operation(summary = "Call remote method.", description = "Call remote investment funds service.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = OpenApiDoc.OK_CODE,
                description = OpenApiDoc.OK_LABEL),
        @ApiResponse(responseCode = OpenApiDoc.BAD_REQUEST_CODE,
                description = OpenApiDoc.BAD_REQUEST_LABEL),
        @ApiResponse(responseCode = OpenApiDoc.INTERNAL_SERVER_ERROR_CODE,
                description = OpenApiDoc.INTERNAL_SERVER_ERROR_LABEL)
    })
    Mono<InvestmentFundsDto> callRemoteClient(
            @Parameter(description = "Procedencia IOs, Android")
            @RequestHeader("channel") @NotNull @NotBlank String channel,
            ServerHttpRequest request
    );

    @Operation(summary = "Call remote method.", description = "Call remote investment funds metadata service.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = OpenApiDoc.OK_CODE,
                description = OpenApiDoc.OK_LABEL),
        @ApiResponse(responseCode = OpenApiDoc.BAD_REQUEST_CODE,
                description = OpenApiDoc.BAD_REQUEST_LABEL),
        @ApiResponse(responseCode = OpenApiDoc.INTERNAL_SERVER_ERROR_CODE,
                description = OpenApiDoc.INTERNAL_SERVER_ERROR_LABEL)
    })
    Mono<MetaDataInvestmentFundsDto> callMetadataRemoteClient(
            @Parameter(description = "Procedencia IOs, Android")
            @RequestHeader("channel") @NotNull @NotBlank String channel,
            ServerHttpRequest request
    );

    @Operation(summary = "Call remote method.", description = "Call remote investment funds service.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = OpenApiDoc.OK_CODE,
                description = OpenApiDoc.OK_LABEL),
        @ApiResponse(responseCode = OpenApiDoc.BAD_REQUEST_CODE,
                description = OpenApiDoc.BAD_REQUEST_LABEL),
        @ApiResponse(responseCode = OpenApiDoc.INTERNAL_SERVER_ERROR_CODE,
                description = OpenApiDoc.INTERNAL_SERVER_ERROR_LABEL)
    })
    Mono<InvestmentFundsDto> callRemoteClientVersion(
            @Parameter(description = "Procedencia IOs, Android")
            @RequestHeader("channel") @NotNull @NotBlank String channel,
            ServerHttpRequest request
    );

    @Operation(summary = "Call remote method.", description = "Call remote investment funds service.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = OpenApiDoc.OK_CODE,
                description = OpenApiDoc.OK_LABEL),
        @ApiResponse(responseCode = OpenApiDoc.BAD_REQUEST_CODE,
                description = OpenApiDoc.BAD_REQUEST_LABEL),
        @ApiResponse(responseCode = OpenApiDoc.INTERNAL_SERVER_ERROR_CODE,
                description = OpenApiDoc.INTERNAL_SERVER_ERROR_LABEL)
    })
    Mono<InvestmentFundsDto> callRemoteClientVersionThree(
            @Parameter(description = "Procedencia IOs, Android")
            @RequestHeader("channel") @NotNull @NotBlank String channel,
            ServerHttpRequest request
    );

    @Operation(summary = "Call remote method.", description = "Call remote investment funds metadata service.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = OpenApiDoc.OK_CODE,
                description = OpenApiDoc.OK_LABEL),
        @ApiResponse(responseCode = OpenApiDoc.BAD_REQUEST_CODE,
                description = OpenApiDoc.BAD_REQUEST_LABEL),
        @ApiResponse(responseCode = OpenApiDoc.INTERNAL_SERVER_ERROR_CODE,
                description = OpenApiDoc.INTERNAL_SERVER_ERROR_LABEL)
    })
    Mono<MetaDataInvestmentFundsDto> callMetadataRemoteClientVersion(
            @Parameter(description = "Procedencia IOs, Android")
            @RequestHeader("channel") @NotNull @NotBlank String channel,
            ServerHttpRequest request
    );

    @Operation(summary = "Call remote method.", description = "Call remote investment funds metadata service.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = OpenApiDoc.OK_CODE,
                description = OpenApiDoc.OK_LABEL),
        @ApiResponse(responseCode = OpenApiDoc.BAD_REQUEST_CODE,
                description = OpenApiDoc.BAD_REQUEST_LABEL),
        @ApiResponse(responseCode = OpenApiDoc.INTERNAL_SERVER_ERROR_CODE,
                description = OpenApiDoc.INTERNAL_SERVER_ERROR_LABEL)
    })
    Mono<MetaDataInvestmentFundsDto> callMetadataRemoteClientVersion2(
            @Parameter(description = "Procedencia IOs, Android")
            ServerHttpRequest request
    );

    @Operation(summary = "Call remote method.", description = "Call remote investment funds metadata service.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = OpenApiDoc.OK_CODE,
                description = OpenApiDoc.OK_LABEL),
        @ApiResponse(responseCode = OpenApiDoc.BAD_REQUEST_CODE,
                description = OpenApiDoc.BAD_REQUEST_LABEL),
        @ApiResponse(responseCode = OpenApiDoc.INTERNAL_SERVER_ERROR_CODE,
                description = OpenApiDoc.INTERNAL_SERVER_ERROR_LABEL)
    })
    Mono<MetaDataInvestmentFundsDto> callMetadataRemoteClientVersion3(
            @Parameter(description = "Procedencia IOs, Android")
            ServerHttpRequest request
    );
}
