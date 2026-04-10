package com.bac.microservice.investmentfunds.utils;

import com.bac.utils.jwt.constant.JwtConstant;
import com.bac.utils.jwt.v2.JwtUtil;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.http.server.reactive.ServerHttpRequest;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class InvestmentFundLogicUtil {

    public static String getUserName(ServerHttpRequest httpRequest) {
        return JwtUtil.getUsername(httpRequest.getHeaders().getFirst(JwtConstant.TOKEN_HEADER));
    }
}
