package com.bac.microservice.investmentfunds.rest.util.mapper;

import com.bac.microservice.investmentfunds.model.alias.AliasCommand;
import com.bac.microservice.investmentfunds.model.alias.AliasResponse;
import com.bac.microservice.investmentfunds.rest.dto.AliasRequestDto;
import com.bac.microservice.investmentfunds.rest.dto.AliasResponseDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(imports = {java.util.UUID.class})
public interface AliasControllerMapper {

    AliasControllerMapper INSTANCE = Mappers.getMapper(AliasControllerMapper.class);

    AliasRequestDto toRequestDto(String alias, String number, String username, String channel, String country);

    AliasCommand toCommand(AliasRequestDto requestDto);

    AliasResponseDto toAliasResponseDto(AliasResponse response);
}
