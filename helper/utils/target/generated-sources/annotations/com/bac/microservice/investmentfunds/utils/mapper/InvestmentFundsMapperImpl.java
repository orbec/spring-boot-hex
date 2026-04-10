package com.bac.microservice.investmentfunds.utils.mapper;

import com.bac.microservice.investmentfunds.model.investmentfunds.InvestmentFundsDto;
import com.bac.microservice.investmentfunds.model.investmentfunds.InvestmentFundsResponse;
import java.util.List;
import javax.annotation.processing.Generated;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-04-10T16:44:05-0500",
    comments = "version: 1.6.3, compiler: javac, environment: Java 17.0.10 (Oracle Corporation)"
)
public class InvestmentFundsMapperImpl implements InvestmentFundsMapper {

    @Override
    public InvestmentFundsDto toInvestmentFundsDto(InvestmentFundsResponse investmentFundsResponse) {
        if ( investmentFundsResponse == null ) {
            return null;
        }

        InvestmentFundsDto.InvestmentFundsDtoBuilder investmentFundsDto = InvestmentFundsDto.builder();

        investmentFundsDto.data( messageToResult( investmentFundsResponse.getMessage() ) );

        InvestmentFundsDto investmentFundsDtoResult = investmentFundsDto.build();

        calculateBalanceSummary( investmentFundsDtoResult );

        return investmentFundsDtoResult;
    }

    @Override
    public InvestmentFundsDto.Result.Fund mapFundToDto(InvestmentFundsResponse.Message.Body.CustomerList.CustomerFund.Fund fund) {
        if ( fund == null ) {
            return null;
        }

        InvestmentFundsDto.Result.Fund fund1 = new InvestmentFundsDto.Result.Fund();

        fund1.setAlias( fund.getFundName() );
        fund1.setNumber( fund.getPortfolioCode() );
        fund1.setLocalBalance( toBigDecimal( fund.getBalance() ) );
        fund1.setAvailableBalance( toBigDecimal( fund.getBalance() ) );
        fund1.setLocalCurrency( fund.getCurrencyCode() );
        fund1.setCif( fund.getCifCode() );
        fund1.setConversionCode( fund.getFundCode() );

        fund1.setCountry( "CR" );

        return fund1;
    }

    private List<InvestmentFundsResponse.Message.Body.CustomerList.CustomerFund> messageBodyCustomerListCustomerFund(InvestmentFundsResponse.Message message) {
        InvestmentFundsResponse.Message.Body body = message.getBody();
        if ( body == null ) {
            return null;
        }
        InvestmentFundsResponse.Message.Body.CustomerList customerList = body.getCustomerList();
        if ( customerList == null ) {
            return null;
        }
        return customerList.getCustomerFund();
    }

    protected InvestmentFundsDto.Result messageToResult(InvestmentFundsResponse.Message message) {
        if ( message == null ) {
            return null;
        }

        InvestmentFundsDto.Result result = new InvestmentFundsDto.Result();

        List<InvestmentFundsResponse.Message.Body.CustomerList.CustomerFund> customerFund = messageBodyCustomerListCustomerFund( message );
        result.setInvestmentFunds( mapCustomerFundsToDto( customerFund ) );

        return result;
    }
}
