package com.bac.microservice.investmentfunds.model.port.in;

import com.bac.microservice.investmentfunds.model.investmentfunds.InvestmentFundsResponse;
import com.bac.microservice.investmentfunds.model.investmentfunds.InvestmentsFundsCommand;
import com.bac.microservice.investmentfunds.model.port.UseCase;

public interface InvestmentFundsPort extends UseCase<InvestmentsFundsCommand, InvestmentFundsResponse> {

}
