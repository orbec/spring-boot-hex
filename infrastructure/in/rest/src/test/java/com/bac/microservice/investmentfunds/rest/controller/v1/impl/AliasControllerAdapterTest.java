package com.bac.microservice.investmentfunds.rest.controller.v1.impl;

import com.bac.microservice.investmentfunds.model.alias.AliasResponse;
import com.bac.microservice.investmentfunds.model.port.in.AliasPort;
import com.bac.microservice.investmentfunds.rest.dto.AliasRequestDto;
import com.bac.microservice.investmentfunds.utils.InvestmentFundLogicUtil;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@WebFluxTest(controllers = AliasControllerAdapter.class)
@ContextConfiguration(classes = AliasControllerAdapter.class)
class AliasControllerAdapterTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockitoBean(name = "investmentFundAliasService")
    private AliasPort investmentFundAliasService;

    @MockitoBean(name = "aliasServiceSecureDecorator")
    private AliasPort investmentFundAliasServiceSecured;

    private AliasRequestDto buildRequest() {
        return AliasRequestDto.builder()
                .alias("myAlias").build();
    }

    private AliasResponse buildResponse() {
        return AliasResponse.builder()
                .alias("myAlias")
                .build();
    }


    @Test
    @DisplayName("POST /{number}/alias should use default alias service")
    void shouldInsertAliasUsingDefaultService() {

        when(investmentFundAliasService.execute(any()))
                .thenReturn(Mono.just(buildResponse()));

        try (MockedStatic<InvestmentFundLogicUtil> mocked =
                     mockStatic(InvestmentFundLogicUtil.class)) {

            mocked.when(() -> InvestmentFundLogicUtil.getUserName(any()))
                    .thenReturn("testUser");

            webTestClient.post()
                    .uri("/123456/alias?country=CR")
                    .contentType(MediaType.APPLICATION_JSON)
                    .header("channel", "WEB")
                    .bodyValue(buildRequest())
                    .exchange()
                    .expectStatus().isOk()
                    .expectBody()
                    .jsonPath("$.alias").isEqualTo("myAlias");
        }

        verify(investmentFundAliasService).execute(any());
        verifyNoInteractions(investmentFundAliasServiceSecured);
    }


    @Test
    @DisplayName("POST /{number}/alias with accept-version=1.0.0 uses default service")
    void shouldInsertAliasWithVersionOne() {

        when(investmentFundAliasService.execute(any()))
                .thenReturn(Mono.just(buildResponse()));

        try (MockedStatic<InvestmentFundLogicUtil> mocked =
                     mockStatic(InvestmentFundLogicUtil.class)) {

            mocked.when(() -> InvestmentFundLogicUtil.getUserName(any()))
                    .thenReturn("testUser");

            webTestClient.post()
                    .uri("/123456/alias?country=CR")
                    .contentType(MediaType.APPLICATION_JSON)
                    .header("channel", "WEB")
                    .header("accept-version", "1.0.0")
                    .bodyValue(buildRequest())
                    .exchange()
                    .expectStatus().isOk()
                    .expectBody()
                    .jsonPath("$.alias").isEqualTo("myAlias");
        }

        verify(investmentFundAliasService).execute(any());
        verifyNoInteractions(investmentFundAliasServiceSecured);
    }


    @Test
    @DisplayName("POST /{number}/alias with accept-version=3.0.0 uses secured service")
    void shouldInsertAliasWithVersionThreeUsingSecuredService() {

        when(investmentFundAliasServiceSecured.execute(any()))
                .thenReturn(Mono.just(buildResponse()));

        try (MockedStatic<InvestmentFundLogicUtil> mocked =
                     mockStatic(InvestmentFundLogicUtil.class)) {

            mocked.when(() -> InvestmentFundLogicUtil.getUserName(any()))
                    .thenReturn("testUser");

            webTestClient.post()
                    .uri("/123456/alias?country=CR")
                    .contentType(MediaType.APPLICATION_JSON)
                    .header("channel", "WEB")
                    .header("accept-version", "3.0.0")
                    .bodyValue(buildRequest())
                    .exchange()
                    .expectStatus().isOk()
                    .expectBody()
                    .jsonPath("$.alias").isEqualTo("myAlias");
        }

        verify(investmentFundAliasServiceSecured).execute(any());
        verifyNoInteractions(investmentFundAliasService);
    }
}