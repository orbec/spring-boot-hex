package com.bac.microservice.investmentfunds.rest.handler;

import com.bac.microservice.investmentfunds.model.investmentfunds.InvestmentFundsDto;
import com.bac.microservice.investmentfunds.exception.InternalServerErrorException;
import com.bac.microservice.investmentfunds.exception.InvestmentFundOwnerException;
import com.bac.utils.exceptions.CommonExceptionHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import reactor.core.publisher.Mono;

import java.util.List;

@RestControllerAdvice
@Slf4j
@RequiredArgsConstructor
public class GlobalExceptionHandler extends CommonExceptionHandler {

    @ExceptionHandler(InvestmentFundOwnerException.class)
    public Mono<ResponseEntity<InvestmentFundsDto>> handleInvestmentFundOwnerErrors(InvestmentFundOwnerException ex) {
        log.error(ex.getMessage());
        InvestmentFundsDto.Errors.Error errorItem = InvestmentFundsDto.Errors.Error.builder()
                .code(ex.getCode())
                .description(ex.getDescription())
                .build();
        InvestmentFundsDto investmentFundsDto = InvestmentFundsDto
                .builder()
                .errors(List.of(InvestmentFundsDto.Errors.builder().error(errorItem).build()))
                .build();
        return Mono.just(new ResponseEntity<>(investmentFundsDto, new HttpHeaders(), HttpStatus.FORBIDDEN));
    }

    @ExceptionHandler(InternalServerErrorException.class)
    public Mono<ResponseEntity<InvestmentFundsDto>> handleInternalErrors(InternalServerErrorException ex) {
        log.error(ex.getMessage());
        InvestmentFundsDto.Errors.Error errorItem = InvestmentFundsDto.Errors.Error.builder()
                .code(ex.getCode())
                .description(ex.getDescription())
                .build();
        InvestmentFundsDto investmentFundsDto = InvestmentFundsDto
                .builder()
                .errors(List.of(InvestmentFundsDto.Errors.builder().error(errorItem).build()))
                .build();
        return Mono.just(new ResponseEntity<>(investmentFundsDto, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR));
    }
}
