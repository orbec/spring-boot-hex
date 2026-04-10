package com.bac.microservice.investmentfunds.rest.controller.v1.impl;

import com.bac.lib.observability.annotations.LogExecution;
import com.bac.microservice.investmentfunds.model.alias.AliasResponse;
import com.bac.microservice.investmentfunds.rest.dto.AliasRequestDto;
import com.bac.microservice.investmentfunds.rest.dto.AliasResponseDto;
import com.bac.microservice.investmentfunds.rest.controller.v1.AliasController;
import com.bac.microservice.investmentfunds.model.port.in.AliasPort;
import com.bac.microservice.investmentfunds.rest.util.mapper.AliasControllerMapper;
import com.bac.microservice.investmentfunds.utils.InvestmentFundLogicUtil;
import com.bac.microservice.utilslogs.utils.SecureLogger;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
public class AliasControllerAdapter implements AliasController {

    private final SecureLogger log = new SecureLogger(AliasControllerAdapter.class);
    private static final AliasControllerMapper MAPPER = AliasControllerMapper.INSTANCE;

    @Qualifier("investmentFundAliasService")
    private final AliasPort investmentFundAliasService;
    @Qualifier("aliasServiceSecureDecorator")
    private final AliasPort investmentFundAliasServiceSecured;

    @PostMapping("/{number}/alias")
    @LogExecution(actionId = "bd-ms-if-alias-new-upd")
    @Override
    public Mono<ResponseEntity<AliasResponseDto>> insertOrUpdateInvestmentFundAlias(
            @PathVariable("number") @NotNull @NotBlank String number,
            @RequestParam("country") @NotNull @NotBlank String country,
            @RequestBody @Valid AliasRequestDto alias,
            @Parameter(description = "Procedencia: IOs, Android")
            @RequestHeader("channel") String channel,
            ServerHttpRequest serverHttpRequest
    ) {
        return insertOrUpdateInvestmentFundAliasLogic(number, country, alias, channel, serverHttpRequest);
    }

    @PostMapping(value = "/{number}/alias", headers = "accept-version=1.0.0")
    @LogExecution(actionId = "bd-ms-if-alias-new-upd")
    public Mono<ResponseEntity<AliasResponseDto>> insertOrUpdateInvestmentFundAliasVersion(
            @PathVariable("number") @NotNull @NotBlank String number,
            @RequestParam("country") @NotNull @NotBlank String country,
            @RequestBody @Valid AliasRequestDto alias,
            @Parameter(description = "Procedencia: IOs, Android")
            @RequestHeader("channel") String channel,
            ServerHttpRequest serverHttpRequest
    ) {
        return insertOrUpdateInvestmentFundAliasLogic(number, country, alias, channel, serverHttpRequest);
    }

    @PostMapping(value = "/{number}/alias", headers = "accept-version=3.0.0")
    @LogExecution(actionId = "bd-ms-if-alias-new-upd")
    public Mono<ResponseEntity<AliasResponseDto>> insertOrUpdateInvestmentFundAliasVersionThree(
        @PathVariable("number") @NotNull @NotBlank String number,
        @RequestParam("country") @NotNull @NotBlank String country,
        @RequestBody @Valid AliasRequestDto alias,
        @Parameter(description = "Procedencia: IOs, Android")
        @RequestHeader("channel") String channel,
        ServerHttpRequest serverHttpRequest
    ) {
        return insertOrUpdateInvestmentFundAliasLogicSecured(number, country, alias, channel, serverHttpRequest);
    }

    private Mono<ResponseEntity<AliasResponseDto>> insertOrUpdateInvestmentFundAliasLogic(
            String number, String country, AliasRequestDto alias, String channel, ServerHttpRequest request) {
        String userName = InvestmentFundLogicUtil.getUserName(request);
        return Mono.fromCallable(() -> MAPPER.toRequestDto(alias.getAlias(), number, userName, channel, country))
                .map(MAPPER::toCommand)
                .doOnSuccess(unused -> log.info("Inserting new alias for investment fund: {}", number))
                .flatMap(aliasCommand -> (Mono<AliasResponse>) investmentFundAliasService.execute(aliasCommand))
                .map(MAPPER::toAliasResponseDto)
                .map(ResponseEntity::ok);
    }

    private Mono<ResponseEntity<AliasResponseDto>> insertOrUpdateInvestmentFundAliasLogicSecured(
        String number, String country, AliasRequestDto alias, String channel, ServerHttpRequest request) {
        String userName = InvestmentFundLogicUtil.getUserName(request);
        return Mono.fromCallable(() -> MAPPER.toRequestDto(alias.getAlias(), number, userName, channel, country))
                .map(MAPPER::toCommand)
                .doOnSuccess(unused -> log.info("Inserting new alias for investment fund: {}", number))
                .flatMap(aliasCommand -> (Mono<AliasResponse>) investmentFundAliasServiceSecured.execute(aliasCommand))
                .map(MAPPER::toAliasResponseDto)
                .map(ResponseEntity::ok);
    }
}