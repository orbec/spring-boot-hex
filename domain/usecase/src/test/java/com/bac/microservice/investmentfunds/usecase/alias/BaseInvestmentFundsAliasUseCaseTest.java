package com.bac.microservice.investmentfunds.usecase.alias;

import com.bac.microservice.investmentfunds.model.alias.AliasCommand;
import com.bac.microservice.investmentfunds.model.alias.InvestmentFundForAlias;
import com.bac.microservice.investmentfunds.model.alias.ProductAlias;
import com.bac.microservice.investmentfunds.model.enums.ResponseCodeEnum;
import com.bac.microservice.investmentfunds.model.port.out.AliasServicePort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BaseInvestmentFundsAliasUseCaseTest {

    @Mock
    private AliasServicePort aliasServicePort;

    private AliasCommand aliasCommand;

    @BeforeEach
    void setUp() {
        aliasCommand = new AliasCommand(
                "my-alias",
                "123456",
                "user",
                "channel",
                "CR"
        );
    }

    static class TestAliasUseCase extends BaseInvestmentFundsAliasUseCase {

        private final Mono<InvestmentFundForAlias> investmentFundMono;

        TestAliasUseCase(AliasServicePort aliasServicePort,
                         Mono<InvestmentFundForAlias> investmentFundMono) {
            super(aliasServicePort);
            this.investmentFundMono = investmentFundMono;
        }

        @Override
        protected Mono<InvestmentFundForAlias> getInvestmentFund(AliasCommand aliasRequest) {
            return investmentFundMono;
        }
    }

    @Test
    @DisplayName("Should create alias successfully when investment fund exists")
    void shouldCreateAliasSuccessfully() {

        InvestmentFundForAlias fund = new InvestmentFundForAlias(
                "123456",
                "CIF123",
                "CONV01",
                "0001"
        );

        ProductAlias storedAlias = ProductAlias.builder()
                .alias("my-alias")
                .build();

        when(aliasServicePort.insertAlias(any(ProductAlias.class)))
                .thenReturn(Mono.just(storedAlias));

        BaseInvestmentFundsAliasUseCase useCase =
                new TestAliasUseCase(aliasServicePort, Mono.just(fund));

        StepVerifier.create(useCase.execute(aliasCommand))
                .assertNext(response ->
                        assertThat(response.getAlias()).isEqualTo("my-alias"))
                .verifyComplete();

        verify(aliasServicePort).insertAlias(any(ProductAlias.class));
    }

    @Test
    @DisplayName("Should return forbidden error when investment fund is not found")
    void shouldReturnForbiddenWhenInvestmentFundNotFound() {

        BaseInvestmentFundsAliasUseCase useCase =
                new TestAliasUseCase(aliasServicePort, Mono.empty());

        StepVerifier.create(useCase.execute(aliasCommand))
                .expectError(com.bac.microservice.investmentfunds.exception.InvestmentFundOwnerException.class)
                .verify();

        verifyNoInteractions(aliasServicePort);
    }

    @Test
    @DisplayName("Should return internal server error when alias service fails")
    void shouldReturnInternalServerErrorWhenAliasServiceFails() {
        InvestmentFundForAlias fund = new InvestmentFundForAlias(
                "123456",
                "CIF123",
                "CONV01",
                "0001"
        );

        when(aliasServicePort.insertAlias(any(ProductAlias.class)))
                .thenReturn(Mono.error(new RuntimeException("DB failure")));

        BaseInvestmentFundsAliasUseCase useCase =
                new TestAliasUseCase(aliasServicePort, Mono.just(fund));

        StepVerifier.create(useCase.execute(aliasCommand))
                .expectError(com.bac.microservice.investmentfunds.exception.InternalServerErrorException.class)
                .verify();

        verify(aliasServicePort).insertAlias(any(ProductAlias.class));
    }

    @Test
    @DisplayName("validateInvestmentFundByNumber should return true when numbers match")
    void validateInvestmentFundByNumberShouldReturnTrue() {
        BaseInvestmentFundsAliasUseCase useCase =
                new TestAliasUseCase(aliasServicePort, Mono.empty());

        assertThat(
                useCase.validateInvestmentFundByNumber(aliasCommand, "123456")
        ).isTrue();
    }

    @Test
    @DisplayName("validateInvestmentFundByNumber should return false when numbers do not match")
    void validateInvestmentFundByNumberShouldReturnFalse() {
        BaseInvestmentFundsAliasUseCase useCase =
                new TestAliasUseCase(aliasServicePort, Mono.empty());

        assertThat(
                useCase.validateInvestmentFundByNumber(aliasCommand, "999999")
        ).isFalse();
    }
}