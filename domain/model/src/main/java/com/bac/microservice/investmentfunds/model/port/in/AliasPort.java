package com.bac.microservice.investmentfunds.model.port.in;

import com.bac.microservice.investmentfunds.model.alias.AliasCommand;
import com.bac.microservice.investmentfunds.model.alias.AliasResponse;
import com.bac.microservice.investmentfunds.model.port.UseCase;

public interface AliasPort extends UseCase<AliasCommand, AliasResponse> {

}
