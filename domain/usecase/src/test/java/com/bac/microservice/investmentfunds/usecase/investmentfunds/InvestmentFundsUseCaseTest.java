package com.bac.microservice.investmentfunds.usecase.investmentfunds;

import com.bac.microservice.investmentfunds.exception.InternalServerErrorException;
import com.bac.microservice.investmentfunds.exception.InvestmentFundOwnerException;
import com.bac.microservice.investmentfunds.model.investmentfunds.InvestmentFundsResponse;
import com.bac.microservice.investmentfunds.model.investmentfunds.InvestmentsFundsCommand;
import com.bac.microservice.investmentfunds.model.port.out.InvestmentFundsServicePort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class InvestmentFundsUseCaseTest {

    @Mock
    private InvestmentFundsServicePort investmentFundsServicePort;

    private InvestmentFundsUseCase useCase;

    private InvestmentsFundsCommand command;

    @BeforeEach
    void setUp() {
        useCase = new InvestmentFundsUseCase(investmentFundsServicePort);
        command = new InvestmentsFundsCommand("CIF123", "CR", "WEB");
    }

    @Test
    @DisplayName("Should return investment funds response when remote service succeeds")
    void shouldReturnResponseWhenServiceSucceeds() {
        InvestmentFundsResponse response = InvestmentFundsResponse.builder().build();

        when(investmentFundsServicePort.callRemoteInvestmentFundsService(command))
                .thenReturn(Mono.just(response));

        StepVerifier.create(useCase.execute(command))
                .expectNext(response)
                .verifyComplete();
    }

    @Test
    @DisplayName("Should propagate InvestmentFundOwnerException without wrapping")
    void shouldPropagateInvestmentFundOwnerException() {
        InvestmentFundOwnerException exception =
                new InvestmentFundOwnerException("0001", "owner mismatch");

        when(investmentFundsServicePort.callRemoteInvestmentFundsService(command))
                .thenReturn(Mono.error(exception));

        StepVerifier.create(useCase.execute(command))
                .expectError(InvestmentFundOwnerException.class)
                .verify();
    }

    @Test
    @DisplayName("Should wrap unexpected errors into InternalServerErrorException")
    void shouldWrapUnexpectedExceptionIntoInternalServerError() {
        RuntimeException unexpected = new RuntimeException("DB down");

        when(investmentFundsServicePort.callRemoteInvestmentFundsService(command))
                .thenReturn(Mono.error(unexpected));

        StepVerifier.create(useCase.execute(command))
                .expectError(InternalServerErrorException.class)
                .verify();
    }
}