package com.bac.microservice.investmentfunds.utils.mapper;

import com.bac.microservice.investmentfunds.model.investmentfunds.InvestmentFundsResponse;
import com.bac.microservice.investmentfunds.model.investmentfunds.InvestmentFundsDto;
import com.bac.utils.constants.CountryConstant;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.AfterMapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Mapper()
public interface InvestmentFundsMapper {

    InvestmentFundsMapper INSTANCE = Mappers.getMapper(InvestmentFundsMapper.class);

    @Mapping(source = "message.body.customerList.customerFund", target = "data.investmentFunds")
    InvestmentFundsDto toInvestmentFundsDto(InvestmentFundsResponse investmentFundsResponse);

    @AfterMapping
    default void calculateBalanceSummary(@MappingTarget InvestmentFundsDto investmentFundsDto) {
        if (investmentFundsDto.getData() == null || investmentFundsDto.getData().getInvestmentFunds() == null) {
            return;
        }

        BigDecimal localBalanceTotal = BigDecimal.ZERO;
        BigDecimal localAvailableBalanceTotal = BigDecimal.ZERO;
        String localCurrency = null;

        for (InvestmentFundsDto.Result.Fund fund : investmentFundsDto.getData().getInvestmentFunds()) {
            if (fund.getLocalBalance() != null) {
                localBalanceTotal = localBalanceTotal.add(fund.getLocalBalance());
            }
            if (fund.getAvailableBalance() != null) {
                localAvailableBalanceTotal = localAvailableBalanceTotal.add(fund.getAvailableBalance());
            }
            if (localCurrency == null && fund.getLocalCurrency() != null) {
                localCurrency = fund.getLocalCurrency();
            }

        }

        BigDecimal dollarBalanceTotal = BigDecimal.ZERO;
        BigDecimal dollarAvailableBalanceTotal = BigDecimal.ZERO;
        InvestmentFundsDto.Result.BalanceSummary summary = new InvestmentFundsDto.Result.BalanceSummary();
        summary.setLocalBalanceTotal(localBalanceTotal);
        summary.setLocalAvailableBalanceTotal(localAvailableBalanceTotal);
        summary.setDollarBalanceTotal(dollarBalanceTotal);
        summary.setDollarAvailableBalanceTotal(dollarAvailableBalanceTotal);
        summary.setLocalCurrencySummary(localCurrency);
        summary.setSize(investmentFundsDto.getData().getInvestmentFunds().size());

        investmentFundsDto.getData().setBalanceSummary(summary);
    }


    default Mono<InvestmentFundsDto> toInvestmentFundsDtoMono(
            Mono<InvestmentFundsResponse> investmentFundsResponseMono) {
        return investmentFundsResponseMono.map(this::toInvestmentFundsDto);
    }

    default List<InvestmentFundsDto.Result.Fund> mapCustomerFundsToDto(
            List<InvestmentFundsResponse.Message.Body.CustomerList.CustomerFund> customerFunds) {
        if (Objects.nonNull(customerFunds)) {
            return customerFunds.stream()
                    .flatMap(fund -> fund.getFund().stream())
                    .map(this::mapFundToDto)
                    .toList();
        } else {
            return new ArrayList<>();
        }

    }

    @Mapping(target = "alias", source = "fundName")
    @Mapping(target = "number", source = "portfolioCode")
    @Mapping(target = "localBalance", source = "balance", qualifiedByName = "toBigDecimal")
    @Mapping(target = "availableBalance", source = "balance", qualifiedByName = "toBigDecimal")
    @Mapping(target = "localCurrency", source = "currencyCode")
    @Mapping(target = "country", constant = CountryConstant.COSTARICA)
    @Mapping(target = "cif", source = "cifCode")
    @Mapping(target = "conversionCode", source = "fundCode")

    InvestmentFundsDto.Result.Fund mapFundToDto(InvestmentFundsResponse.Message.Body.CustomerList.CustomerFund
                                                        .Fund fund);

    @Named("toBigDecimal")
    default BigDecimal toBigDecimal(String value) {
        return new BigDecimal(value);
    }
    
}
