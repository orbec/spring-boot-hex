package com.bac.microservice.investmentfunds.utils;

import com.bac.microservice.investmentfunds.model.enums.ResponseCodeEnum;
import com.bac.microservice.investmentfunds.exception.InternalServerErrorException;
import com.bac.microservice.investmentfunds.exception.InvestmentFundOwnerException;

public class ExceptionUtil {

    private ExceptionUtil() {}

    public static InvestmentFundOwnerException createForbiddenException(ResponseCodeEnum responseCodeEnum) {
        return createForbiddenException(responseCodeEnum.name(), responseCodeEnum.getDescription());
    }

    public static InvestmentFundOwnerException createForbiddenException(String code, String description) {
        return new InvestmentFundOwnerException(code, description);
    }

    public static InternalServerErrorException createInternalServerErrorException(ResponseCodeEnum responseCodeEnum) {
        return createInternalServerErrorException(responseCodeEnum.name(), responseCodeEnum.getDescription());
    }

    public static InternalServerErrorException createInternalServerErrorException(String code, String description) {
        return new InternalServerErrorException(code, description);
    }
}
