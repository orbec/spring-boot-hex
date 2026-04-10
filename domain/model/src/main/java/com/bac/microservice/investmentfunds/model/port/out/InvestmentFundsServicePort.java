package com.bac.microservice.investmentfunds.model.port.out;

import com.bac.microservice.investmentfunds.model.investmentfunds.InvestmentFundsResponse;
import com.bac.microservice.investmentfunds.model.investmentfunds.InvestmentsFundsCommand;
import reactor.core.publisher.Mono;

public interface InvestmentFundsServicePort {

    Mono<InvestmentFundsResponse> callRemoteInvestmentFundsService(InvestmentsFundsCommand investmentsFundsCommand);
}
