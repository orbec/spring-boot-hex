package com.bac.microservice.investmentfunds.usecase.investmentfunds;

import com.bac.lib.aliasmanager.dto.ProductAliasDto;
import com.bac.lib.aliasmanager.enums.ProductType;
import com.bac.lib.aliasmanager.service.ProductAliasService;
import com.bac.lib.openfga.dto.AuthAccessDto;
import com.bac.lib.openfga.dto.Data;
import com.bac.microservice.investmentfunds.model.investmentfunds.*;
import com.bac.microservice.investmentfunds.model.openfga.ReducedFgaRequest;
import com.bac.microservice.investmentfunds.model.port.in.InvestmentFundsPort;
import com.bac.utils.constants.CountryConstant;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class InvestmentFundsLobbyUseCaseTest {

    @Mock
    private InvestmentFundsPort investmentFundsService;

    @Mock
    private ProductAliasService productAliasService;

    private InvestmentFundsLobbyUseCase useCase;

    @BeforeEach
    void setUp() {
        useCase = new InvestmentFundsLobbyUseCase(
                investmentFundsService,
                productAliasService
        );
    }

    private ReducedFgaRequest buildReducedRequest(String cif) {

        AuthAccessDto authAccessDto = AuthAccessDto.builder()
                .data(List.of(
                        Data.builder()
                                .country(CountryConstant.COSTARICA)
                                .cifs(List.of(cif))
                                .build()
                ))
                .build();

        return new ReducedFgaRequest("WEB", authAccessDto, "user");
    }

    private InvestmentFundsResponse buildResponse(String fundNumber) {
        InvestmentFundsResponse.Message.Body.CustomerList.CustomerFund.Fund fund =
                InvestmentFundsResponse.Message.Body.CustomerList.CustomerFund.Fund.builder()
                        .fundCode(fundNumber)
                        .cifCode("CIF123")
                        .customerCode("CUSTOMER1")
                        .customerName("Test Customer")
                        .portfolioCode("PORT01")
                        .fundName("Test Fund")
                        .currencyCode("CRC")
                        .localcurrencyCode("CRC")
                        .balance("1000")
                        .participationQuantity("10")
                        .participationPrice("100")
                        .minimumAmount("100")
                        .investmentTime("12")
                        .destinationAccount("ACC123")
                        .yieldRate("5")
                        .numberYieldRateDays("30")
                        .allowsInvest("Y")
                        .allowsWithdrawal("Y")
                        .build();

        InvestmentFundsResponse.Message.Body.CustomerList.CustomerFund customerFund =
                InvestmentFundsResponse.Message.Body.CustomerList.CustomerFund.builder()
                        .customerCode("CUSTOMER1")
                        .fund(List.of(fund))
                        .build();

        InvestmentFundsResponse.Message.Body.CustomerList customerList =
                InvestmentFundsResponse.Message.Body.CustomerList.builder()
                        .customerFund(List.of(customerFund))
                        .build();

        InvestmentFundsResponse.Message.Body body =
                InvestmentFundsResponse.Message.Body.builder()
                        .customerList(customerList)
                        .build();

        InvestmentFundsResponse.Message message =
                InvestmentFundsResponse.Message.builder()
                        .body(body)
                        .build();

        return InvestmentFundsResponse.builder()
                .message(message)
                .build();
    }

    @Test
    @DisplayName("Should return investment funds with alias when data exists")
    void callRemoteInvestmentFundsServiceShouldReturnDataWithAlias() {
        ReducedFgaRequest request = buildReducedRequest("CIF123");

        when(investmentFundsService.execute(any()))
                .thenReturn(Mono.just(buildResponse("123456")));

        when(productAliasService.getProductAliasByUserAndProductType(
                "user", ProductType.INVESTMENTS_FUNDS))
                .thenReturn(Flux.just(
                        new ProductAliasDto(
                        )

                ));

        StepVerifier.create(useCase.callRemoteInvestmentFundsService(request))
                .assertNext(dto -> {
                    String actualNumber = dto.getData().getInvestmentFunds().get(0).getNumber();
                    assertThat(actualNumber).isNotNull();
                })
                .verifyComplete();
    }

    @Test
    @DisplayName("Should return investment funds with empty alias when alias service fails")
    void callRemoteInvestmentFundsServiceShouldIgnoreAliasErrors() {
        ReducedFgaRequest request = buildReducedRequest("CIF123");

        when(investmentFundsService.execute(any()))
                .thenReturn(Mono.just(buildResponse("123456")));

        when(productAliasService.getProductAliasByUserAndProductType(any(), any()))
                .thenReturn(Flux.error(new RuntimeException("Alias service down")));

        StepVerifier.create(useCase.callRemoteInvestmentFundsService(request))
                .assertNext(dto -> {
                    assertThat(dto.getData().getInvestmentFunds()).hasSize(1);
                    assertThat(dto.getData().getInvestmentFunds().get(0).getAlias())
                            .isEqualTo("Test Fund");
                })
                .verifyComplete();
    }

    @Test
    @DisplayName("Should return metadata with hasInvestmentFunds = true when data exists")
    void metadataServiceShouldReturnTrueWhenFundsExist() {
        ReducedFgaRequest request = buildReducedRequest("CIF123");

        when(investmentFundsService.execute(any()))
                .thenReturn(Mono.just(buildResponse("123456")));

        StepVerifier.create(useCase.callInvestmentFundsMetadataService(request))
                .assertNext(meta ->
                        assertThat(meta.getHasInvestmentFunds()).isTrue())
                .verifyComplete();
    }

    @Test
    @DisplayName("Should return metadata with hasInvestmentFunds = false when no data exists")
    void metadataServiceShouldReturnFalseWhenNoFundsExist() {
        ReducedFgaRequest request = buildReducedRequest("CIF123");

        when(investmentFundsService.execute(any()))
                .thenReturn(Mono.just(buildEmptyResponse()));

        StepVerifier.create(useCase.callInvestmentFundsMetadataService(request))
                .assertNext(meta ->
                        assertThat(meta.getHasInvestmentFunds()).isFalse())
                .verifyComplete();
    }

    private InvestmentFundsResponse buildEmptyResponse() {
        InvestmentFundsResponse.Message.Body.CustomerList customerList =
                InvestmentFundsResponse.Message.Body.CustomerList.builder()
                        .customerFund(List.of())
                        .build();

        InvestmentFundsResponse.Message.Body body =
                InvestmentFundsResponse.Message.Body.builder()
                        .customerList(customerList)
                        .build();

        InvestmentFundsResponse.Message message =
                InvestmentFundsResponse.Message.builder()
                        .body(body)
                        .build();

        return InvestmentFundsResponse.builder()
                .message(message)
                .build();
    }
}