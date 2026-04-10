package com.bac.microservice.investmentfunds.config;

import com.bac.lib.aliasmanager.service.ProductAliasService;
import com.bac.lib.openfga.service.fga.OpenFgaService;
import com.bac.microservice.investmentfunds.model.port.in.AliasPort;
import com.bac.microservice.investmentfunds.model.port.in.InvestmentFundsLobbyServicePort;
import com.bac.microservice.investmentfunds.model.port.in.InvestmentFundsPort;
import com.bac.microservice.investmentfunds.model.port.out.AliasServicePort;
import com.bac.microservice.investmentfunds.model.port.out.InvestmentFundsServicePort;
import com.bac.microservice.investmentfunds.rest.decorator.AliasServiceSecureDecorator;
import com.bac.microservice.investmentfunds.rest.decorator.InvestmentFundsLobbyServiceSecureDecorator;
import com.bac.microservice.investmentfunds.usecase.alias.InvestmentFundsAliasUseCase;
import com.bac.microservice.investmentfunds.usecase.investmentfunds.InvestmentFundsLobbyUseCase;
import com.bac.microservice.investmentfunds.usecase.investmentfunds.InvestmentFundsUseCase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackages = "com.bac.microservice.investments")
public class UseCaseConfiguration {

    @Bean ("investmentFundsLobbyService")
    public InvestmentFundsLobbyServicePort investmentFundsLobbyService(
            InvestmentFundsPort investmentFundsServicePort, ProductAliasService productAliasService) {
        return new InvestmentFundsLobbyUseCase(investmentFundsServicePort, productAliasService);
    }

    @Bean("investmentFundsLobbyServiceSecureDecorator")
    public InvestmentFundsLobbyServicePort investmentFundsServiceSecureDecorator(
            InvestmentFundsLobbyServicePort investmentFundsServicePort
    ) {
        return new InvestmentFundsLobbyServiceSecureDecorator(investmentFundsServicePort);
    }

    @Bean("investmentFundAliasService")
    public AliasPort investmentFundAliasService(InvestmentFundsLobbyServicePort investmentFundsLobbyService,
                                                AliasServicePort productAliasService,
                                                OpenFgaService openFgaService) {
        return new InvestmentFundsAliasUseCase(investmentFundsLobbyService, productAliasService, openFgaService);
    }

    @Bean ("aliasServiceSecureDecorator")
    public AliasPort aliasServiceSecureDecorator(AliasPort aliasPort) {
        return new AliasServiceSecureDecorator(aliasPort);
    }

    @Bean ("investmentFundsService")
    public InvestmentFundsPort investmentFundsService(InvestmentFundsServicePort investmentFundsServicePort) {
        return new InvestmentFundsUseCase(investmentFundsServicePort);
    }


}
