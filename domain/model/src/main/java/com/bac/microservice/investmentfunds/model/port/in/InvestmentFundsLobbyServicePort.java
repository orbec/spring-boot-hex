package com.bac.microservice.investmentfunds.model.port.in;

import com.bac.microservice.investmentfunds.model.openfga.ReducedFgaRequest;
import com.bac.microservice.investmentfunds.model.investmentfunds.InvestmentFundsDto;
import com.bac.microservice.investmentfunds.model.investmentfunds.MetaDataInvestmentFundsDto;
import reactor.core.publisher.Mono;

public interface InvestmentFundsLobbyServicePort {

    Mono<InvestmentFundsDto> callRemoteInvestmentFundsService(ReducedFgaRequest reducedRequest);

    Mono<MetaDataInvestmentFundsDto> callInvestmentFundsMetadataService(ReducedFgaRequest reducedRequest);

}
