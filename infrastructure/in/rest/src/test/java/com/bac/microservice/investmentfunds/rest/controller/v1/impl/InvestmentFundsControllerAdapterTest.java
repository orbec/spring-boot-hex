package com.bac.microservice.investmentfunds.rest.controller.v1.impl;

import com.bac.lib.openfga.dto.AuthAccessDto;
import com.bac.lib.openfga.dto.Data;
import com.bac.lib.openfga.service.fga.OpenFgaService;
import com.bac.microservice.investmentfunds.model.investmentfunds.InvestmentFundsDto;
import com.bac.microservice.investmentfunds.model.investmentfunds.MetaDataInvestmentFundsDto;
import com.bac.microservice.investmentfunds.model.port.in.InvestmentFundsLobbyServicePort;
import com.bac.microservice.investmentfunds.utils.InvestmentFundLogicUtil;
import com.bac.utils.parser.Company;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@WebFluxTest(controllers = InvestmentFundsControllerAdapter.class)
@ContextConfiguration(classes = InvestmentFundsControllerAdapter.class)
class InvestmentFundsControllerAdapterTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockitoBean(name = "investmentFundsLobbyService")
    private InvestmentFundsLobbyServicePort lobbyService;

    @MockitoBean(name = "investmentFundsLobbyServiceSecureDecorator")
    private InvestmentFundsLobbyServicePort lobbyServiceSecured;

    @MockitoBean
    private OpenFgaService openFgaService;

    private AuthAccessDto authAccessWithCifs() {
        Data data = Data.builder()
                .country("CR")
                .cifs(List.of("CIF123"))
                .build();

        return AuthAccessDto.builder()
                .data(List.of(data))
                .build();
    }

    @Test
    @DisplayName("GET / uses non-secured investment funds service")
    void shouldUseDefaultInvestmentFundsService() {

        when(openFgaService.getAuthAccessDto((ServerHttpRequest) any(), eq(Company.SFI)))
                .thenReturn(Mono.just(authAccessWithCifs()));

        when(lobbyService.callRemoteInvestmentFundsService(any()))
                .thenReturn(Mono.just(new InvestmentFundsDto()));

        try (MockedStatic<InvestmentFundLogicUtil> mocked =
                     mockStatic(InvestmentFundLogicUtil.class)) {

            mocked.when(() -> InvestmentFundLogicUtil.getUserName(any()))
                    .thenReturn("testUser");

            webTestClient.get()
                    .uri("/")
                    .header("channel", "WEB")
                    .accept(MediaType.APPLICATION_JSON)
                    .exchange()
                    .expectStatus().isOk();
        }

        verify(lobbyService).callRemoteInvestmentFundsService(any());
        verifyNoInteractions(lobbyServiceSecured);
    }

    @Test
    @DisplayName("GET / with accept-version=3.0.0 uses secured service")
    void shouldUseSecuredInvestmentFundsService() {

        when(openFgaService.getAuthAccessDto((ServerHttpRequest) any(), eq(Company.SFI)))
                .thenReturn(Mono.just(authAccessWithCifs()));

        when(lobbyServiceSecured.callRemoteInvestmentFundsService(any()))
                .thenReturn(Mono.just(new InvestmentFundsDto()));

        try (MockedStatic<InvestmentFundLogicUtil> mocked =
                     mockStatic(InvestmentFundLogicUtil.class)) {

            mocked.when(() -> InvestmentFundLogicUtil.getUserName(any()))
                    .thenReturn("testUser");

            webTestClient.get()
                    .uri("/")
                    .header("channel", "WEB")
                    .header("accept-version", "3.0.0")
                    .accept(MediaType.APPLICATION_JSON)
                    .exchange()
                    .expectStatus().isOk();
        }

        verify(lobbyServiceSecured).callRemoteInvestmentFundsService(any());
        verifyNoInteractions(lobbyService);
    }

    @Test
    @DisplayName("GET /metadata uses lobby metadata service")
    void shouldCallMetadataService() {

        when(openFgaService.getAuthAccessDto((ServerHttpRequest) any(), eq(Company.SFI)))
                .thenReturn(Mono.just(authAccessWithCifs()));

        when(lobbyService.callInvestmentFundsMetadataService(any()))
                .thenReturn(Mono.just(new MetaDataInvestmentFundsDto()));

        webTestClient.get()
                .uri("/metadata")
                .header("channel", "WEB")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk();

        verify(lobbyService).callInvestmentFundsMetadataService(any());
    }

    @Test
    @DisplayName("GET /metadata with accept-version=2.0.0 uses OpenFGA only")
    void shouldCallMetadataWithoutDataPower() {

        when(openFgaService.getAuthAccessDto((ServerHttpRequest) any(), eq(Company.SFI)))
                .thenReturn(Mono.just(authAccessWithCifs()));

        webTestClient.get()
                .uri("/metadata")
                .header("accept-version", "2.0.0")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody(MetaDataInvestmentFundsDto.class)
                .value(MetaDataInvestmentFundsDto::getHasInvestmentFunds);

        verifyNoInteractions(lobbyService);
        verifyNoInteractions(lobbyServiceSecured);
    }
}