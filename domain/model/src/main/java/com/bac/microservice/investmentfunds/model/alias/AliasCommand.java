package com.bac.microservice.investmentfunds.model.alias;


public record AliasCommand(String alias, String number, String username, String channel, String country) {
}
