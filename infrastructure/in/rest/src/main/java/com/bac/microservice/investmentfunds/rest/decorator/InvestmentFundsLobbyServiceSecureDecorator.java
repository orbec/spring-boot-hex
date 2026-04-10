package com.bac.microservice.investmentfunds.rest.decorator;

import com.bac.lib.secure.crypto.domain.crypto.CryptoAlgorithm;
import com.bac.lib.secure.crypto.domain.crypto.annotation.SecureCrypto;
import com.bac.microservice.investmentfunds.model.openfga.ReducedFgaRequest;
import com.bac.microservice.investmentfunds.model.investmentfunds.InvestmentFundsDto;
import com.bac.microservice.investmentfunds.model.investmentfunds.MetaDataInvestmentFundsDto;
import com.bac.microservice.investmentfunds.model.port.in.InvestmentFundsLobbyServicePort;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class InvestmentFundsLobbyServiceSecureDecorator implements InvestmentFundsLobbyServicePort {
    private final InvestmentFundsLobbyServicePort investmentFundsLobbyService;

    @Override
    @SecureCrypto(CryptoAlgorithm.AES)
    public Mono<InvestmentFundsDto> callRemoteInvestmentFundsService(ReducedFgaRequest reducedRequest) {
        return investmentFundsLobbyService.callRemoteInvestmentFundsService(reducedRequest);
    }

    @Override
    @SecureCrypto(CryptoAlgorithm.AES)
    public Mono<MetaDataInvestmentFundsDto> callInvestmentFundsMetadataService(ReducedFgaRequest reducedRequest) {
        return investmentFundsLobbyService.callInvestmentFundsMetadataService(reducedRequest);
    }
}
