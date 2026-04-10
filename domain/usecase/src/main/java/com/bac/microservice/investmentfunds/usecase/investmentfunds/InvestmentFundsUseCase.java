package com.bac.microservice.investmentfunds.usecase.investmentfunds;

import com.bac.microservice.investmentfunds.exception.InternalServerErrorException;
import com.bac.microservice.investmentfunds.exception.InvestmentFundOwnerException;
import com.bac.microservice.investmentfunds.model.investmentfunds.InvestmentFundsResponse;
import com.bac.microservice.investmentfunds.model.investmentfunds.InvestmentsFundsCommand;
import com.bac.microservice.investmentfunds.model.port.in.InvestmentFundsPort;
import com.bac.microservice.investmentfunds.model.port.out.InvestmentFundsServicePort;
import com.bac.microservice.utilslogs.utils.SecureLogger;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;


@RequiredArgsConstructor
public class InvestmentFundsUseCase implements InvestmentFundsPort {

    private final InvestmentFundsServicePort investmentFundsServicePort;
    private static final SecureLogger log = new SecureLogger(InvestmentFundsUseCase.class);

    @Override
    public Mono<InvestmentFundsResponse> execute(InvestmentsFundsCommand investmentsFundsCommand) {

        return investmentFundsServicePort.callRemoteInvestmentFundsService(investmentsFundsCommand)
                .doOnError(error -> log.error("error in investment funds"))
                .onErrorResume(throwable -> throwable instanceof InvestmentFundOwnerException exception
                        ? Mono.error(exception)
                        : Mono.error(new InternalServerErrorException("internal error", throwable.getMessage())));

    }

}
