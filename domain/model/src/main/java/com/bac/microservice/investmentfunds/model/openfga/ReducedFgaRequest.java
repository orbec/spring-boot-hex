package com.bac.microservice.investmentfunds.model.openfga;

import com.bac.lib.openfga.dto.AuthAccessDto;

public record ReducedFgaRequest(
        String channel,
        AuthAccessDto authAccessDto,
        String username

) {
}
