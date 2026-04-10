package com.bac.microservice.investmentfunds.model.investmentfunds;

public record InvestmentsFundsCommand(
        String clientCode,
        String country,
        String channel

) {
}
