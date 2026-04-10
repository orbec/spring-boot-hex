package com.bac.microservice.investmentfunds.usecase.investmentfunds;

import com.bac.lib.aliasmanager.dto.ProductAliasDto;
import com.bac.lib.aliasmanager.enums.ProductType;
import com.bac.lib.aliasmanager.service.ProductAliasService;
import com.bac.microservice.investmentfunds.model.investmentfunds.InvestmentFundsResponse;
import com.bac.microservice.investmentfunds.model.openfga.ReducedFgaRequest;
import com.bac.microservice.investmentfunds.model.investmentfunds.InvestmentsFundsCommand;
import com.bac.microservice.investmentfunds.model.investmentfunds.InvestmentFundsDto;
import com.bac.microservice.investmentfunds.model.investmentfunds.MetaDataInvestmentFundsDto;
import com.bac.microservice.investmentfunds.model.port.in.InvestmentFundsLobbyServicePort;
import com.bac.microservice.investmentfunds.model.port.in.InvestmentFundsPort;
import com.bac.microservice.investmentfunds.utils.mapper.InvestmentFundsMapper;
import com.bac.microservice.utilslogs.utils.SecureLogger;
import com.bac.utils.constants.CountryConstant;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Primary
public class InvestmentFundsLobbyUseCase implements InvestmentFundsLobbyServicePort {

    private final SecureLogger log = new SecureLogger(InvestmentFundsLobbyUseCase.class);

    private final InvestmentFundsPort investmentFundsService;
    private final ProductAliasService productAliasService;


    @Override
    public Mono<InvestmentFundsDto> callRemoteInvestmentFundsService(ReducedFgaRequest reducedRequest) {
        List<Mono<InvestmentFundsResponse>> investmentFundsResponseList = new ArrayList<>();

        reducedRequest.authAccessDto().getData().forEach(authAccess -> {
            if (authAccess.getCountry().equals(CountryConstant.COSTARICA)) {
                authAccess.getCifs().forEach(cif -> {
                    InvestmentsFundsCommand investmentsFundsCommandInternal =
                            new InvestmentsFundsCommand(cif, authAccess.getCountry(), reducedRequest.channel());
                    investmentFundsResponseList.add((Mono<InvestmentFundsResponse>)
                            investmentFundsService.execute(investmentsFundsCommandInternal)
                    );
                });
            }
        });

        Mono<List<InvestmentFundsResponse>> investmentFundsListMono = Flux.concat(investmentFundsResponseList)
                .collectList();

        return investmentFundsListMono.flatMap(pensionFundsResponses -> {
            InvestmentFundsDto investmentFundsDto = buildItem(pensionFundsResponses);
            return setAlias(investmentFundsDto, reducedRequest.username());
        });
    }

    @Override
    public Mono<MetaDataInvestmentFundsDto> callInvestmentFundsMetadataService(ReducedFgaRequest reducedRequest) {
        List<Mono<InvestmentFundsResponse>> investmentFundsResponseList = new ArrayList<>();

        reducedRequest.authAccessDto().getData().forEach(authAccess -> {
            if (authAccess.getCountry().equals(CountryConstant.COSTARICA)) {
                authAccess.getCifs().forEach(cif -> {
                    InvestmentsFundsCommand investmentsFundsCommandInternal =
                            new InvestmentsFundsCommand(cif, authAccess.getCountry(), reducedRequest.channel());
                    investmentFundsResponseList.add((Mono<InvestmentFundsResponse>)
                            investmentFundsService.execute(investmentsFundsCommandInternal)
                    );
                });
            }
        });

        Mono<List<InvestmentFundsResponse>> pensionFundsListMono = Flux.concat(investmentFundsResponseList)
                .collectList();

        return pensionFundsListMono.flatMap(pensionFundsResponses -> {
            InvestmentFundsDto pensionFundsDto = buildItem(pensionFundsResponses);
            MetaDataInvestmentFundsDto metadataInvestmentFundsDtoMono = new MetaDataInvestmentFundsDto();
            if (Objects.nonNull(pensionFundsDto.getData().getInvestmentFunds()) && CollectionUtils
                    .isNotEmpty(pensionFundsDto.getData().getInvestmentFunds())) {
                metadataInvestmentFundsDtoMono.setHasInvestmentFunds(Boolean.TRUE);
            } else {
                metadataInvestmentFundsDtoMono.setHasInvestmentFunds(Boolean.FALSE);
            }
            return Mono.just(metadataInvestmentFundsDtoMono);
        });
    }

    private InvestmentFundsDto buildItem(List<InvestmentFundsResponse> pensionFundsResponseList) {
        InvestmentFundsDto pensionFundsDto = new InvestmentFundsDto();
        InvestmentFundsDto.Result data = new InvestmentFundsDto.Result();
        pensionFundsDto.setData(data);
        pensionFundsDto.getData().setBalanceSummary(new InvestmentFundsDto.Result.BalanceSummary());
        data.setInvestmentFunds(new ArrayList<>());

        for (InvestmentFundsResponse pensionFundsResponse : pensionFundsResponseList) {
            if (pensionFundsResponse != null) {
                InvestmentFundsDto pensionFundsDtoInternal = InvestmentFundsMapper.INSTANCE
                        .toInvestmentFundsDto(pensionFundsResponse);
                pensionFundsDto.getData().setBalanceSummary(pensionFundsDtoInternal.getData().getBalanceSummary());
                if (Objects.nonNull(pensionFundsDtoInternal)
                        && Objects.nonNull(pensionFundsDtoInternal.getData())
                        && CollectionUtils.isNotEmpty(pensionFundsDtoInternal.getData().getInvestmentFunds())) {
                    pensionFundsDto.getData().getInvestmentFunds().addAll(pensionFundsDtoInternal.getData()
                            .getInvestmentFunds());
                }
            }
        }
        pensionFundsDto.getData().getBalanceSummary().setSize(CollectionUtils.size(data.getInvestmentFunds()));

        return pensionFundsDto;
    }

    private Mono<InvestmentFundsDto> setAlias(InvestmentFundsDto investmentFundDto, String username) {
        return productAliasService.getProductAliasByUserAndProductType(username, ProductType.INVESTMENTS_FUNDS)
            .collectMap(ProductAliasDto::getProductNumber, ProductAliasDto::getAlias)
            .map(generatedAlias -> {
                investmentFundDto.getData().getInvestmentFunds()
                    .forEach(fund -> fund.setAlias(generatedAlias.getOrDefault(fund.getNumber(), "")));
                return investmentFundDto;
            })
            .onErrorResume(throwable -> {
                log.error("Error getting alias, leaving empty alias", throwable);
                return Mono.just(investmentFundDto);
            });
    }
}
