package com.bac.microservice.investmentfunds.usecase.alias;

import com.bac.lib.openfga.dto.AuthAccessDto;
import com.bac.lib.openfga.dto.Data;
import com.bac.lib.openfga.service.fga.OpenFgaService;
import com.bac.microservice.investmentfunds.model.alias.AliasCommand;
import com.bac.microservice.investmentfunds.model.investmentfunds.InvestmentFundsDto;
import com.bac.microservice.investmentfunds.model.investmentfunds.InvestmentFundsDto.Result.Fund;
import com.bac.microservice.investmentfunds.model.port.in.InvestmentFundsLobbyServicePort;
import com.bac.microservice.investmentfunds.model.port.out.AliasServicePort;
import com.bac.utils.parser.Company;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class InvestmentFundsAliasUseCaseTest {

    @Mock
    private InvestmentFundsLobbyServicePort lobbyService;

    @Mock
    private AliasServicePort aliasServicePort;

    @Mock
    private OpenFgaService openFgaService;

    private InvestmentFundsAliasUseCase useCase;

    private AliasCommand aliasCommand;

    @BeforeEach
    void setUp() {
        useCase = new InvestmentFundsAliasUseCase(
                lobbyService,
                aliasServicePort,
                openFgaService
        );

        aliasCommand = new AliasCommand(
                "alias",
                "123456",
                "user",
                "WEB",
                "CR"
        );
    }

    @Test
    @DisplayName("Should return investment fund when matching number exists")
    void shouldReturnInvestmentFundWhenFound() {
        Data fgaData = Data.builder()
                .country("CR")
                .build();

        AuthAccessDto authAccessDto = AuthAccessDto.builder()
                .data(List.of(fgaData))
                .build();

        when(openFgaService.getAuthAccessDto("user", Company.SFI))
                .thenReturn(Mono.just(authAccessDto));

        Fund fund = new Fund();
        fund.setNumber("123456");
        fund.setCif("CIF123");
        fund.setCountry("CR");
        fund.setConversionCode("CONV01");

        InvestmentFundsDto dto = new InvestmentFundsDto();
        dto.setData(new InvestmentFundsDto.Result());
        dto.getData().setInvestmentFunds(List.of(fund));

        when(lobbyService.callRemoteInvestmentFundsService(any()))
                .thenReturn(Mono.just(dto));

        StepVerifier.create(useCase.getInvestmentFund(aliasCommand))
                .assertNext(result -> {
                    assertThat(result.number()).isEqualTo("123456");
                    assertThat(result.cif()).isEqualTo("CIF123");
                    assertThat(result.conversionCode()).isEqualTo("CONV01");
                })
                .verifyComplete();
    }

    @Test
    @DisplayName("Should return empty when investment fund number does not match")
    void shouldReturnEmptyWhenInvestmentNotFound() {
        AuthAccessDto authAccessDto = AuthAccessDto.builder()
                .data(List.of(Data.builder().country("CR").build()))
                .build();

        when(openFgaService.getAuthAccessDto("user", Company.SFI))
                .thenReturn(Mono.just(authAccessDto));

        Fund fund = new Fund();
        fund.setNumber("999999");

        InvestmentFundsDto dto = new InvestmentFundsDto();
        dto.setData(new InvestmentFundsDto.Result());
        dto.getData().setInvestmentFunds(List.of(fund));

        when(lobbyService.callRemoteInvestmentFundsService(any()))
                .thenReturn(Mono.just(dto));

        StepVerifier.create(useCase.getInvestmentFund(aliasCommand))
                .verifyComplete();
    }

    @Test
    @DisplayName("Should return empty when no FGA data matches country")
    void shouldReturnEmptyWhenCountryNotAuthorized() {
        AuthAccessDto authAccessDto = AuthAccessDto.builder()
                .data(List.of(Data.builder().country("US").build()))
                .build();

        when(openFgaService.getAuthAccessDto("user", Company.SFI))
                .thenReturn(Mono.just(authAccessDto));


        when(lobbyService.callRemoteInvestmentFundsService(any()))
                .thenReturn(Mono.just(buildInvestmentFundsDto(List.of())));


        StepVerifier.create(useCase.getInvestmentFund(aliasCommand))
                .verifyComplete();
    }

    private InvestmentFundsDto buildInvestmentFundsDto(List<Fund> funds) {
        InvestmentFundsDto dto = new InvestmentFundsDto();
        InvestmentFundsDto.Result data = new InvestmentFundsDto.Result();
        data.setInvestmentFunds(funds);
        dto.setData(data);
        return dto;
    }
}
