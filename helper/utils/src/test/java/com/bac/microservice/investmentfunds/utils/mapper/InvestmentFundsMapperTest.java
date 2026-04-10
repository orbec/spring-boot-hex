package com.bac.microservice.investmentfunds.utils.mapper;

import com.bac.microservice.investmentfunds.model.investmentfunds.InvestmentFundsDto;
import com.bac.microservice.investmentfunds.model.investmentfunds.InvestmentFundsResponse;
import com.bac.utils.constants.CountryConstant;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class InvestmentFundsMapperTest {

    private final InvestmentFundsMapper mapper = InvestmentFundsMapper.INSTANCE;

    private InvestmentFundsResponse buildResponseWithFunds(List<String> balances) {
        List<InvestmentFundsResponse.Message.Body.CustomerList.CustomerFund> customerFunds =
                balances.stream().map(balance -> {

                    InvestmentFundsResponse.Message.Body.CustomerList.CustomerFund.Fund fund =
                            InvestmentFundsResponse.Message.Body.CustomerList.CustomerFund.Fund.builder()
                                    .fundCode("FUND123")
                                    .portfolioCode("PORT123")
                                    .fundName("Test Fund")
                                    .balance(balance)
                                    .currencyCode("CRC")
                                    .localcurrencyCode("CRC")
                                    .cifCode("CIF123")
                                    .build();

                    return InvestmentFundsResponse.Message.Body.CustomerList.CustomerFund.builder()
                            .customerCode("CUST01")
                            .fund(List.of(fund))
                            .build();
                }).toList();

        InvestmentFundsResponse.Message.Body.CustomerList customerList =
                InvestmentFundsResponse.Message.Body.CustomerList.builder()
                        .customerFund(customerFunds)
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
    @DisplayName("Should map a single investment fund correctly")
    void shouldMapSingleFund() {
        InvestmentFundsResponse response =
                buildResponseWithFunds(List.of("1000"));

        InvestmentFundsDto dto =
                mapper.toInvestmentFundsDto(response);

        assertThat(dto.getData().getInvestmentFunds()).hasSize(1);

        InvestmentFundsDto.Result.Fund fund =
                dto.getData().getInvestmentFunds().get(0);

        assertThat(fund.getAlias()).isEqualTo("Test Fund");
        assertThat(fund.getNumber()).isEqualTo("PORT123");
        assertThat(fund.getCif()).isEqualTo("CIF123");
        assertThat(fund.getConversionCode()).isEqualTo("FUND123");
        assertThat(fund.getCountry()).isEqualTo(CountryConstant.COSTARICA);

        assertThat(fund.getLocalBalance())
                .isEqualByComparingTo(new BigDecimal("1000"));
    }

    @Test
    @DisplayName("Should calculate balance summary for multiple funds")
    void shouldCalculateBalanceSummary() {
        InvestmentFundsResponse response =
                buildResponseWithFunds(List.of("500", "1500"));

        InvestmentFundsDto dto =
                mapper.toInvestmentFundsDto(response);

        InvestmentFundsDto.Result.BalanceSummary summary =
                dto.getData().getBalanceSummary();

        assertThat(summary.getLocalBalanceTotal())
                .isEqualByComparingTo("2000");

        assertThat(summary.getLocalAvailableBalanceTotal())
                .isEqualByComparingTo("2000");

        assertThat(summary.getDollarBalanceTotal())
                .isEqualByComparingTo("0");

        assertThat(summary.getDollarAvailableBalanceTotal())
                .isEqualByComparingTo("0");

        assertThat(summary.getSize()).isEqualTo(2);
        assertThat(summary.getLocalCurrencySummary()).isEqualTo("CRC");
    }

    @Test
    @DisplayName("Should return empty fund list when customerFund is empty")
    void shouldReturnEmptyFundList() {
        InvestmentFundsResponse response =
                InvestmentFundsResponse.builder()
                        .message(InvestmentFundsResponse.Message.builder()
                                .body(InvestmentFundsResponse.Message.Body.builder()
                                        .customerList(
                                                InvestmentFundsResponse.Message.Body.CustomerList.builder()
                                                        .customerFund(List.of())
                                                        .build()
                                        )
                                        .build()
                                )
                                .build()
                        )
                        .build();

        InvestmentFundsDto dto =
                mapper.toInvestmentFundsDto(response);

        assertThat(dto.getData().getInvestmentFunds()).isEmpty();
        assertThat(dto.getData().getBalanceSummary().getSize()).isZero();
    }

    @Test
    @DisplayName("Should handle null customerFund gracefully")
    void shouldHandleNullCustomerFund() {
        InvestmentFundsResponse response =
                InvestmentFundsResponse.builder()
                        .message(InvestmentFundsResponse.Message.builder()
                                .body(InvestmentFundsResponse.Message.Body.builder()
                                        .customerList(
                                                InvestmentFundsResponse.Message.Body.CustomerList.builder()
                                                        .customerFund(null)
                                                        .build()
                                        )
                                        .build()
                                )
                                .build()
                        )
                        .build();

        InvestmentFundsDto dto =
                mapper.toInvestmentFundsDto(response);

        assertThat(dto.getData().getInvestmentFunds()).isEmpty();
        assertThat(dto.getData().getBalanceSummary().getSize()).isZero();
    }

    @Test
    @DisplayName("Should convert string values to BigDecimal")
    void shouldConvertStringToBigDecimal() {
        BigDecimal result =
                mapper.toBigDecimal("12345");

        assertThat(result)
                .isEqualByComparingTo("12345");
    }

    @Test
    @DisplayName("Should map mono wrapper correctly")
    void shouldMapMono() {
        InvestmentFundsResponse response =
                buildResponseWithFunds(List.of("100"));

        InvestmentFundsDto dto =
                mapper.toInvestmentFundsDtoMono(
                        reactor.core.publisher.Mono.just(response)
                ).block();

        assertThat(dto).isNotNull();
        assertThat(dto.getData().getInvestmentFunds()).hasSize(1);
    }
}