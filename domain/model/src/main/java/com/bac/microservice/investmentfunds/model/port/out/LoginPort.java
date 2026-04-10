package com.bac.microservice.investmentfunds.model.port.out;

import com.bac.lib.oauth.domain.LoginTokenRequest;

public interface LoginPort {

    LoginTokenRequest buildLoginParamsLobbyInvestmentFunds();
}
