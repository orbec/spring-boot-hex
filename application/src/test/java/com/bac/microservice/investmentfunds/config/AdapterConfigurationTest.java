package com.bac.microservice.investmentfunds.config;

import com.bac.lib.aliasmanager.service.ProductAliasService;
import com.bac.lib.datapower.sdk.DataPowerResource;
import com.bac.lib.oauth.sdk.OAuthResource;
import com.bac.microservice.investmentfunds.model.port.out.AliasServicePort;
import com.bac.microservice.investmentfunds.model.port.out.InvestmentFundsServicePort;
import com.bac.microservice.investmentfunds.model.port.out.LoginPort;
import com.google.gson.Gson;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.reactive.function.client.WebClient;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class AdapterConfigurationTest {

    private AdapterConfiguration configuration;

    @Mock
    private DataPowerResource dataPowerResource;

    @Mock
    private OAuthResource oauthResource;

    @Mock
    private ProductAliasService productAliasService;

    private Gson gson;
    private WebClient.Builder webClientBuilder;

    @BeforeEach
    void setUp() {
        configuration = new AdapterConfiguration();

        gson = new Gson();
        webClientBuilder = WebClient.builder();

        ReflectionTestUtils.setField(configuration, "investmentFundsPath", "/funds");
        ReflectionTestUtils.setField(configuration, "server", "server");
        ReflectionTestUtils.setField(configuration, "operationCode", "OP");
        ReflectionTestUtils.setField(configuration, "user", "user");
        ReflectionTestUtils.setField(configuration, "appEnvironment", "TEST");
        ReflectionTestUtils.setField(configuration, "serviceUrl", "https://dp.test");
        ReflectionTestUtils.setField(configuration, "isActiveAccountOauth", true);

        ReflectionTestUtils.setField(configuration, "grantType", "client_credentials");
        ReflectionTestUtils.setField(configuration, "scope", "scope");
        ReflectionTestUtils.setField(configuration, "clientId", "client-id");
        ReflectionTestUtils.setField(configuration, "clientSecret", "client-secret");
        ReflectionTestUtils.setField(configuration, "baseUrlLogin", "https://login.test");
    }

    @Test
    @DisplayName("Should create Data Power WebClient")
    void shouldCreateDpWebClient() {
        WebClient webClient = configuration.webClient(webClientBuilder);

        assertThat(webClient).isNotNull();
    }

    @Test
    @DisplayName("Should create LoginAdapter")
    void shouldCreateLoginAdapter() {
        LoginPort loginPort = configuration.loginAdapter();

        assertThat(loginPort).isNotNull();
    }

    @Test
    @DisplayName("Should create investmentFundsServiceDatapowerAdapter")
    void shouldCreateInvestmentFundsServiceDatapowerAdapter() {
        WebClient webClient = configuration.webClient(webClientBuilder);
        LoginPort loginPort = configuration.loginAdapter();

        InvestmentFundsServicePort service =
                configuration.investmentFundsServiceDatapowerAdapter(
                        dataPowerResource,
                        gson,
                        oauthResource,
                        webClient,
                        loginPort
                );

        assertThat(service).isNotNull();
    }

    @Test
    @DisplayName("Should create aliasServiceAdapter")
    void shouldCreateAliasServiceAdapter() {
        AliasServicePort aliasServicePort =
                configuration.aliasServicePort(productAliasService);

        assertThat(aliasServicePort).isNotNull();
    }
}