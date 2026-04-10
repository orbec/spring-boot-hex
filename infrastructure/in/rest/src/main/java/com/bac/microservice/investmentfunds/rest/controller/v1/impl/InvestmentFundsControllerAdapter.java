package com.bac.microservice.investmentfunds.rest.controller.v1.impl;

import com.bac.lib.observability.annotations.LogExecution;
import com.bac.lib.openfga.dto.AuthAccessDto;
import com.bac.lib.openfga.service.fga.OpenFgaService;
import com.bac.microservice.investmentfunds.model.openfga.ReducedFgaRequest;
import com.bac.microservice.investmentfunds.model.investmentfunds.InvestmentFundsDto;
import com.bac.microservice.investmentfunds.model.investmentfunds.MetaDataInvestmentFundsDto;
import com.bac.microservice.investmentfunds.rest.controller.v1.InvestmentFundsController;
import com.bac.microservice.investmentfunds.model.port.in.InvestmentFundsLobbyServicePort;
import com.bac.microservice.investmentfunds.utils.InvestmentFundLogicUtil;
import com.bac.utils.parser.Company;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.server.RequestPath;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
@Slf4j
public class InvestmentFundsControllerAdapter implements InvestmentFundsController {

    @Qualifier("investmentFundsLobbyService")
    private final InvestmentFundsLobbyServicePort investmentFundsLobbyServicePort;
    @Qualifier("investmentFundsLobbyServiceSecureDecorator")
    private final InvestmentFundsLobbyServicePort investmentFundsLobbyServicePortSecured;
    private final OpenFgaService openFgaService;

    @GetMapping
    @LogExecution(actionId = "bd-ms-if-con")
    @Override
    public Mono<InvestmentFundsDto> callRemoteClient(
            @Parameter(description = "Procedencia IOs, Android")
            @RequestHeader("channel") @NotNull @NotBlank String channel,
            ServerHttpRequest request
    ) {
        return callRemoteClientLogic(channel, request);
    }

    @GetMapping("/metadata")
    @LogExecution(actionId = "bd-ms-if-mtd")
    @Override
    public Mono<MetaDataInvestmentFundsDto> callMetadataRemoteClient(
            @Parameter(description = "Procedencia IOs, Android")
            @RequestHeader("channel") @NotNull @NotBlank String channel,
            ServerHttpRequest request
    ) {
        return  callMetadataRemoteClientLogic(channel, request);
    }

    @GetMapping(headers = "accept-version=1.0.0")
    @LogExecution(actionId = "bd-ms-if-con")
    @Override
    public Mono<InvestmentFundsDto> callRemoteClientVersion(
            @Parameter(description = "Procedencia IOs, Android")
            @RequestHeader("channel") @NotNull @NotBlank String channel,
            ServerHttpRequest request
    ) {
        return callRemoteClientLogic(channel, request);
    }

    @GetMapping(headers = "accept-version=3.0.0")
    @LogExecution(actionId = "bd-ms-if-con")
    @Override
    public Mono<InvestmentFundsDto> callRemoteClientVersionThree(
        @Parameter(description = "Procedencia IOs, Android")
        @RequestHeader("channel") @NotNull @NotBlank String channel,
        ServerHttpRequest request
    ) {
        return callRemoteClientLogicSecured(channel, request);
    }

    @GetMapping(value = "/metadata", headers = "accept-version=1.0.0")
    @LogExecution(actionId = "bd-ms-if-mtd")
    @Operation(summary = "Call remote method.", description = "Call remote investment funds metadata service.")
    @Override
    public Mono<MetaDataInvestmentFundsDto> callMetadataRemoteClientVersion(
            @Parameter(description = "Procedencia IOs, Android")
            @RequestHeader("channel") @NotNull @NotBlank String channel,
            ServerHttpRequest request
    ) {
        return  callMetadataRemoteClientLogic(channel, request);
    }

    @GetMapping(value = "/metadata", headers = "accept-version=2.0.0")
    @LogExecution(actionId = "bd-ms-if-mtd")
    @Override
    public Mono<MetaDataInvestmentFundsDto> callMetadataRemoteClientVersion2(
            @Parameter(description = "Procedencia IOs, Android")
            ServerHttpRequest request
    ) {
        return  callMetadataRemoteClientLogicWithoutDataPower(request);
    }

    @GetMapping(value = "/metadata", headers = "accept-version=3.0.0")
    @LogExecution(actionId = "bd-ms-if-mtd")
    @Override
    public Mono<MetaDataInvestmentFundsDto> callMetadataRemoteClientVersion3(
        @Parameter(description = "Procedencia IOs, Android")
        ServerHttpRequest request
    ) {
        return  callMetadataRemoteClientLogicWithoutDataPower(request);
    }

    private Mono<MetaDataInvestmentFundsDto> callMetadataRemoteClientLogicWithoutDataPower(ServerHttpRequest request) {
        logUsedVersion(request.getHeaders().getFirst("accept-version"),
                request.getPath());
        MetaDataInvestmentFundsDto metaDataInvestmentFundsDto = new MetaDataInvestmentFundsDto();
        Mono<AuthAccessDto> authAccessDtoMono = openFgaService.getAuthAccessDto(
                request,
                Company.SFI);
        return authAccessDtoMono
            .map(authAccessDto -> {
                boolean hasInvestmentFunds = authAccessDto.getData() != null
                    && !authAccessDto.getData().isEmpty()
                    && authAccessDto.getData().get(0).getCifs() != null
                    && !authAccessDto.getData().get(0).getCifs().isEmpty();
                metaDataInvestmentFundsDto.setHasInvestmentFunds(hasInvestmentFunds);
                return metaDataInvestmentFundsDto;
            })
            .onErrorResume(e -> {
                metaDataInvestmentFundsDto.setHasInvestmentFunds(null);
                return Mono.just(metaDataInvestmentFundsDto);
            });
    }

    private Mono<MetaDataInvestmentFundsDto> callMetadataRemoteClientLogic(String channel, ServerHttpRequest request) {
        logUsedVersion(request.getHeaders().getFirst("accept-version"),
                request.getPath());
        Mono<AuthAccessDto> authAccessDtoMono = openFgaService.getAuthAccessDto(
                request,
                Company.SFI);

        return authAccessDtoMono.flatMap(authAccessDto -> {
            ReducedFgaRequest consolidatedFgaRequest = new ReducedFgaRequest(
                    channel,
                    authAccessDto,
                    ""
            );
            return investmentFundsLobbyServicePort.callInvestmentFundsMetadataService(consolidatedFgaRequest);
        });
    }

    private Mono<InvestmentFundsDto> callRemoteClientLogic(String channel, ServerHttpRequest request) {
        String userName = InvestmentFundLogicUtil.getUserName(request);
        Mono<AuthAccessDto> authAccessDtoMono = openFgaService.getAuthAccessDto(
                request,
                Company.SFI);

        return authAccessDtoMono.flatMap(authAccessDto -> {

            ReducedFgaRequest consolidatedFgaRequest = new ReducedFgaRequest(
                    channel,
                    authAccessDto,
                    userName
            );
            return investmentFundsLobbyServicePort.callRemoteInvestmentFundsService(consolidatedFgaRequest);
        });
    }

    private Mono<InvestmentFundsDto> callRemoteClientLogicSecured(String channel, ServerHttpRequest request) {
        String userName = InvestmentFundLogicUtil.getUserName(request);
        Mono<AuthAccessDto> authAccessDtoMono = openFgaService.getAuthAccessDto(
            request,
            Company.SFI);

        return authAccessDtoMono.flatMap(authAccessDto -> {
            ReducedFgaRequest consolidatedFgaRequest = new ReducedFgaRequest(
                channel,
                authAccessDto,
                userName
            );
            return investmentFundsLobbyServicePortSecured.callRemoteInvestmentFundsService(consolidatedFgaRequest);
        });
    }

    private void logUsedVersion(String version, RequestPath path) {
        log.info("Version = {} used for path = {}", version, path);
    }
}
