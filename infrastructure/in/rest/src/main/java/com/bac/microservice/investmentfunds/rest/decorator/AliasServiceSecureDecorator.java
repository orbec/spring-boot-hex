package com.bac.microservice.investmentfunds.rest.decorator;

import com.bac.lib.secure.crypto.domain.crypto.CryptoAlgorithm;
import com.bac.lib.secure.crypto.domain.crypto.annotation.SecureCrypto;
import com.bac.microservice.investmentfunds.model.alias.AliasCommand;
import com.bac.microservice.investmentfunds.model.alias.AliasResponse;
import com.bac.microservice.investmentfunds.model.port.in.AliasPort;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class AliasServiceSecureDecorator implements AliasPort {
    private final AliasPort aliasService;

    @SecureCrypto(CryptoAlgorithm.AES)
    public Mono<AliasResponse> execute(AliasCommand alias) {
        return (Mono<AliasResponse>) aliasService.execute(alias);
    }
}
