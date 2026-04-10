package com.bac.microservice.investmentfunds.config;

import com.bac.lib.aliasmanager.service.ProductAliasService;
import com.bac.lib.openfga.service.fga.OpenFgaService;
import com.bac.microservice.investmentfunds.model.port.in.AliasPort;
import com.bac.microservice.investmentfunds.model.port.in.InvestmentFundsLobbyServicePort;
import com.bac.microservice.investmentfunds.model.port.in.InvestmentFundsPort;
import com.bac.microservice.investmentfunds.model.port.out.AliasServicePort;
import com.bac.microservice.investmentfunds.model.port.out.InvestmentFundsServicePort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class UseCaseConfigurationTest {

    private UseCaseConfiguration configuration;

    @Mock
    private InvestmentFundsPort investmentFundsPort;

    @Mock
    private InvestmentFundsServicePort investmentFundsServicePort;

    @Mock
    private InvestmentFundsLobbyServicePort investmentFundsLobbyServicePort;

    @Mock
    private ProductAliasService productAliasService;

    @Mock
    private AliasServicePort aliasServicePort;

    @Mock
    private OpenFgaService openFgaService;

    @Mock
    private AliasPort aliasPort;

    @BeforeEach
    void setUp() {
        configuration = new UseCaseConfiguration();
    }

    @Test
    @DisplayName("Should create investmentFundsLobbyUseCase")
    void shouldCreateInvestmentFundsLobbyUseCase() {
        InvestmentFundsLobbyServicePort service =
                configuration.investmentFundsLobbyService(
                        investmentFundsPort,
                        productAliasService
                );

        assertThat(service).isNotNull();
    }

    @Test
    @DisplayName("Should create investmentFundsLobbyServiceSecureDecorator")
    void shouldCreateInvestmentFundsLobbyServiceSecureDecorator() {
        InvestmentFundsLobbyServicePort secureDecorator =
                configuration.investmentFundsServiceSecureDecorator(
                        investmentFundsLobbyServicePort
                );

        assertThat(secureDecorator).isNotNull();
    }

    @Test
    @DisplayName("Should create investmentFundsAliasUseCase")
    void shouldCreateInvestmentFundsAliasUseCase() {
        AliasPort aliasUseCase =
                configuration.investmentFundAliasService(
                        investmentFundsLobbyServicePort,
                        aliasServicePort,
                        openFgaService
                );

        assertThat(aliasUseCase).isNotNull();
    }

    @Test
    @DisplayName("Should create aliasServiceSecureDecorator")
    void shouldCreateAliasServiceSecureDecorator() {
        AliasPort secureDecorator =
                configuration.aliasServiceSecureDecorator(aliasPort);

        assertThat(secureDecorator).isNotNull();
    }

    @Test
    @DisplayName("Should create investmentFundsUseCase")
    void shouldCreateInvestmentFundsUseCase() {
        InvestmentFundsPort service =
                configuration.investmentFundsService(
                        investmentFundsServicePort
                );

        assertThat(service).isNotNull();
    }
}
