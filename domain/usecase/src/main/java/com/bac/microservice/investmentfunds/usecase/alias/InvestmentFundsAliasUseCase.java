package com.bac.microservice.investmentfunds.usecase.alias;


import com.bac.lib.openfga.dto.AuthAccessDto;
import com.bac.lib.openfga.dto.Data;
import com.bac.lib.openfga.service.fga.OpenFgaService;
import com.bac.microservice.investmentfunds.model.alias.AliasCommand;
import com.bac.microservice.investmentfunds.model.alias.InvestmentFundForAlias;
import com.bac.microservice.investmentfunds.model.openfga.ReducedFgaRequest;
import com.bac.microservice.investmentfunds.model.investmentfunds.InvestmentFundsDto;
import com.bac.microservice.investmentfunds.model.port.in.InvestmentFundsLobbyServicePort;
import com.bac.microservice.investmentfunds.model.port.out.AliasServicePort;
import com.bac.utils.parser.Company;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.function.Function;

public class InvestmentFundsAliasUseCase extends BaseInvestmentFundsAliasUseCase {

    private final InvestmentFundsLobbyServicePort investmentFundsLobbyService;

    private final OpenFgaService openFgaService;

    public InvestmentFundsAliasUseCase(InvestmentFundsLobbyServicePort investmentFundsLobbyService,
                                       AliasServicePort productAliasService, OpenFgaService openFgaService) {
        super(productAliasService);
        this.investmentFundsLobbyService = investmentFundsLobbyService;
        this.openFgaService = openFgaService;
    }

    @Override
    protected Mono<InvestmentFundForAlias> getInvestmentFund(AliasCommand aliasRequest) {
        return openFgaService.getAuthAccessDto(aliasRequest.username(), Company.SFI)
                .map(authAccessDto -> buildAuthAccessDto(authAccessDto, aliasRequest.country()))
                .map(authAccessDto -> buildReducedFgaRequest(aliasRequest.channel(), authAccessDto,
                    aliasRequest.username()))
                .flatMap(investmentFundsLobbyService::callRemoteInvestmentFundsService)
                .flatMap(findInvestmentFundById(aliasRequest));
    }

    private Function<InvestmentFundsDto, Mono<InvestmentFundForAlias>> findInvestmentFundById(
            AliasCommand aliasRequest) {
        return dto -> Mono.justOrEmpty(
                dto.getData().getInvestmentFunds().stream()
                        .filter(investment -> this.validateInvestmentFundByNumber(aliasRequest,
                                investment.getNumber()))
                        .findFirst()
                        .map(investment -> new InvestmentFundForAlias(investment.getNumber(), investment.getCif(),
                                investment.getCountry(), investment.getConversionCode()))
        );
    }

    private AuthAccessDto buildAuthAccessDto(AuthAccessDto authAccessDto, String country) {
        List<Data> data = authAccessDto.getData().stream()
                .filter(dataItem -> country.equals(dataItem.getCountry()))
                .toList();
        authAccessDto.setData(data);
        return AuthAccessDto.builder()
                .data(data)
                .build();
    }

    private ReducedFgaRequest buildReducedFgaRequest(String channel, AuthAccessDto authAccessDto, String username) {
        return new ReducedFgaRequest(channel, authAccessDto, username);
    }
}
