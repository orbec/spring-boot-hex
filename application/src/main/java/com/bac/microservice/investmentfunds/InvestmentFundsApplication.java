package com.bac.microservice.investmentfunds;

import jakarta.annotation.PostConstruct;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories;
import org.springframework.web.reactive.config.EnableWebFlux;
import reactor.core.publisher.Hooks;

@SpringBootApplication
@EnableR2dbcRepositories(basePackages = "com.bac.lib.aliasmanager.repository")
@EnableWebFlux
public class InvestmentFundsApplication {

    public static void main(String[] args) {
        SpringApplication.run(InvestmentFundsApplication.class, args);
    }

    @PostConstruct
    public void init() {
        Hooks.enableAutomaticContextPropagation();
    }
}
