package com.bac.microservice.investmentfunds.model.enums;

import lombok.Getter;

@Getter
public enum ResponseCodeEnum {

    IFNT0001("Investment fund not found"),
    IFNT0002("Error inserting alias");

    private final String description;

    ResponseCodeEnum(String description) {
        this.description = description;
    }
}
