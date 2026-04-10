package com.bac.microservice.investmentfunds.exception;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class InternalServerErrorException extends RuntimeException {
    private final String code;
    private final String description;
}
