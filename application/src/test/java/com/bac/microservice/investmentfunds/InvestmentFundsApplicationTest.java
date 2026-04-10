package com.bac.microservice.investmentfunds;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.springframework.boot.SpringApplication;
import reactor.core.publisher.Hooks;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mockStatic;

class InvestmentFundsApplicationTest {

    @AfterEach
    void tearDown() {
        Hooks.resetOnEachOperator();
    }

    @Test
    @DisplayName("Main method should start Spring application")
    void mainShouldRunSpringApplication() {
        try (MockedStatic<SpringApplication> springApplicationMock =
                     mockStatic(SpringApplication.class)) {

            springApplicationMock
                    .when(() -> SpringApplication.run(
                            InvestmentFundsApplication.class,
                            new String[]{}))
                    .thenReturn(null);

            // Execute
            InvestmentFundsApplication.main(new String[]{});

            // Verify
            springApplicationMock.verify(
                    () -> SpringApplication.run(
                            InvestmentFundsApplication.class,
                            new String[]{}));
        }
    }

    @Test
    @DisplayName("Init method should enable Reactor automatic context propagation")
    void initShouldEnableReactorContextPropagation() {
        InvestmentFundsApplication application = new InvestmentFundsApplication();
        application.init();
        assertThat(Hooks.isAutomaticContextPropagationEnabled()).isTrue();
    }
}