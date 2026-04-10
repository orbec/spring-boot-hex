package com.bac.microservice.investmentfunds.config;

import com.bac.lib.aliasmanager.service.ProductAliasService;
import com.bac.lib.datapower.sdk.DataPowerResource;
import com.bac.lib.oauth.sdk.OAuthResource;
import com.bac.microservice.investmentfunds.model.port.out.AliasServicePort;
import com.bac.microservice.investmentfunds.model.port.out.InvestmentFundsServicePort;
import com.bac.microservice.investmentfunds.model.port.out.LoginPort;
import com.bac.microservice.investments.oauth.LoginAdapter;
import com.bac.microservice.investments.datapower.InvestmentFundsServiceDatapowerAdapter;
import com.bac.microservice.investments.persistence.AliasServiceAdapter;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
@ComponentScan(basePackages = "com.bac")
public class AdapterConfiguration {

    @Value("${remote.endpoint.service-data-power.lobby-investment-funds.service-path}")
    private String investmentFundsPath;
    @Value("${remote.endpoint.service-data-power.server}")
    private String server;
    @Value("${remote.endpoint.service-data-power.lobby-investment-funds.operation-code}")
    private String operationCode;
    @Value("${remote.endpoint.service-data-power.lobby-investment-funds.user}")
    private String user;

    @Value("${application.environment}")
    private String appEnvironment;

    @Value("${remote.endpoint.service-data-power.lobby-investment-funds.base-url}")
    private String serviceUrl;

    @Value("${remote.endpoint.service-login.services.lobby-investment-funds.active}")
    private boolean isActiveAccountOauth;

    @Value("${remote.endpoint.service-login.services.lobby-investment-funds.query-params.grant-type}")
    private String grantType;
    @Value("${remote.endpoint.service-login.services.lobby-investment-funds.query-params.scope}")
    private String scope;
    @Value("${remote.endpoint.service-login.services.lobby-investment-funds.query-params.client-id}")
    private String clientId;
    @Value("${remote.endpoint.service-login.services.lobby-investment-funds.query-params.client-secret}")
    private String clientSecret;
    @Value("${remote.endpoint.service-login.services.lobby-investment-funds.url}")
    private String baseUrlLogin;

    @Bean(name = "dpWebClient")
    public WebClient webClient(@Qualifier("WebClientBuilderSSL") WebClient.Builder webClientBuilder) {
        return webClientBuilder.baseUrl(serviceUrl).build();
    }

    @Bean
    public LoginPort loginAdapter() {
        return new LoginAdapter(
                baseUrlLogin,
                grantType,
                scope,
                clientId,
                clientSecret
        );
    }

    @Bean
    public InvestmentFundsServicePort investmentFundsServiceDatapowerAdapter(DataPowerResource remoteClient,
                                                                             Gson gson,
                                                                             OAuthResource oauthResource,
                                                                             @Qualifier("dpWebClient")
                                                                                  WebClient webClient,
                                                                              LoginPort loginUtil) {
        return new InvestmentFundsServiceDatapowerAdapter(remoteClient,
                gson,
                oauthResource,
                webClient,
                loginUtil,
                investmentFundsPath,
                server,
                operationCode,
                user,
                appEnvironment,
                isActiveAccountOauth);
    }

    @Bean
    public AliasServicePort aliasServicePort(ProductAliasService productAliasService) {
        return new AliasServiceAdapter(productAliasService);
    }

}
