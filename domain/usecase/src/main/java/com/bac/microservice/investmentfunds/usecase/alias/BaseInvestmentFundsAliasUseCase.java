package com.bac.microservice.investmentfunds.usecase.alias;


import com.bac.microservice.investmentfunds.model.alias.ProductAlias;
import com.bac.lib.aliasmanager.enums.ProductType;
import com.bac.microservice.investmentfunds.model.alias.AliasCommand;
import com.bac.microservice.investmentfunds.model.alias.AliasResponse;
import com.bac.microservice.investmentfunds.model.alias.InvestmentFundForAlias;
import com.bac.microservice.investmentfunds.model.enums.ResponseCodeEnum;
import com.bac.microservice.investmentfunds.model.port.in.AliasPort;
import com.bac.microservice.investmentfunds.model.port.out.AliasServicePort;
import com.bac.microservice.utilslogs.utils.SecureLogger;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

import static com.bac.microservice.investmentfunds.utils.ExceptionUtil.createForbiddenException;
import static com.bac.microservice.investmentfunds.utils.ExceptionUtil.createInternalServerErrorException;


@RequiredArgsConstructor
public abstract class BaseInvestmentFundsAliasUseCase implements AliasPort {
    private final SecureLogger log = new SecureLogger(BaseInvestmentFundsAliasUseCase.class);


    private final AliasServicePort aliasServicePort;

    protected abstract Mono<InvestmentFundForAlias> getInvestmentFund(AliasCommand aliasRequest);

    public Mono<AliasResponse> execute(AliasCommand aliasRequest) {
        log.info("Inserting alias for investment fund: {}", aliasRequest.number());

        return this.getInvestmentFund(aliasRequest)
                .switchIfEmpty(Mono.defer(() -> Mono.error(createForbiddenException(ResponseCodeEnum.IFNT0001))))
                .flatMap(investment -> createOrUpdateAlias(investment, aliasRequest)
                        .map(productAlias -> AliasResponse.builder()
                            .alias(productAlias.getAlias())
                            .build())
                        .doOnSuccess(v -> log.info("Alias inserted successfully for investment fund : {}",
                                aliasRequest.number()))
                        .doOnError(e -> log.error("Error inserting alias for investment fund: {}: {}",
                                aliasRequest.number(), e.getCause().getMessage()))
                        .onErrorResume(aliasResponse -> Mono.error(
                                createInternalServerErrorException(ResponseCodeEnum.IFNT0002))
                        ));
    }

    private Mono<ProductAlias> createOrUpdateAlias(InvestmentFundForAlias investmentFund,
                                                   AliasCommand aliasRequest) {
        ProductAlias productAlias = ProductAlias.builder()
                .username(aliasRequest.username())
                .channel(aliasRequest.channel())
                .countryCd(aliasRequest.country())
                .cifCd(investmentFund.cif())
                .productNumber(investmentFund.number())
                .alias(aliasRequest.alias())
                .conversionCode(investmentFund.conversionCode())
                .productType(ProductType.INVESTMENTS_FUNDS)
                .isActive(true)
                .build();

        return aliasServicePort.insertAlias(productAlias);
    }

    protected boolean validateInvestmentFundByNumber(AliasCommand aliasRequest, String number) {
        return aliasRequest.number().equals(number);
    }


}
