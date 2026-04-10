package com.bac.microservice.investmentfunds.rest.handler;

import com.bac.microservice.investmentfunds.exception.InternalServerErrorException;
import com.bac.microservice.investmentfunds.exception.InvestmentFundOwnerException;
import com.bac.microservice.investmentfunds.model.investmentfunds.InvestmentFundsDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import static org.assertj.core.api.Assertions.assertThat;

@WebFluxTest
@ContextConfiguration(classes = {
        GlobalExceptionHandler.class,
        GlobalExceptionHandlerTest.TestController.class
})
class GlobalExceptionHandlerTest {

    @Autowired
    private WebTestClient webTestClient;

    @Test
    @DisplayName("InvestmentFundOwnerException should be translated to HTTP 403")
    void shouldReturnForbiddenForInvestmentFundOwnerException() {

        webTestClient.get()
                .uri("/test/forbidden")
                .exchange()
                .expectStatus().isEqualTo(HttpStatus.FORBIDDEN)
                .expectBody(InvestmentFundsDto.class)
                .value(dto -> {
                    assertThat(dto.getErrors()).isNotNull();
                    assertThat(dto.getErrors()).hasSize(1);
                    assertThat(dto.getErrors().get(0).getError().getCode())
                            .isEqualTo("IFNT0001");
                    assertThat(dto.getErrors().get(0).getError().getDescription())
                            .isEqualTo("Forbidden business rule");
                });
    }

    @Test
    @DisplayName("InternalServerErrorException should be translated to HTTP 500")
    void shouldReturnInternalServerErrorForInternalException() {

        webTestClient.get()
                .uri("/test/internal")
                .exchange()
                .expectStatus().isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR)
                .expectBody(InvestmentFundsDto.class)
                .value(dto -> {
                    assertThat(dto.getErrors()).isNotNull();
                    assertThat(dto.getErrors()).hasSize(1);
                    assertThat(dto.getErrors().get(0).getError().getCode())
                            .isEqualTo("IFNT0002");
                    assertThat(dto.getErrors().get(0).getError().getDescription())
                            .isEqualTo("Internal processing error");
                });
    }

    @RestController
    static class TestController {

        @GetMapping("/test/forbidden")
        public Mono<Void> forbidden() {
            return Mono.error(
                    new InvestmentFundOwnerException(
                            "IFNT0001",
                            "Forbidden business rule"
                    )
            );
        }

        @GetMapping("/test/internal")
        public Mono<Void> internal() {
            return Mono.error(
                    new InternalServerErrorException(
                            "IFNT0002",
                            "Internal processing error"
                    )
            );
        }
    }
}