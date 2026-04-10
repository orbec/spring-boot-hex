package com.bac.microservice.investmentfunds.model.port.out;

import com.bac.microservice.investmentfunds.model.alias.ProductAlias;
import reactor.core.publisher.Mono;

public interface AliasServicePort {

    Mono<ProductAlias> insertAlias(ProductAlias productAlias);



}
