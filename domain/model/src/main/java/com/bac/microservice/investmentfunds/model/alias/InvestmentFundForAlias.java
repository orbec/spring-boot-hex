package com.bac.microservice.investmentfunds.model.alias;

public record InvestmentFundForAlias(
        String number,
        String cif,
        String country,
        String conversionCode
) {
}
