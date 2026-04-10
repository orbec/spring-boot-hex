package com.bac.microservice.investmentfunds.rest.dto;

import com.bac.lib.secure.crypto.domain.crypto.annotation.DecryptField;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AliasRequestDto {


    @Pattern(regexp = "^[a-zA-ZñÑ0-9 ]+$", message = "can only contain letters and numbers.")
    @Size(max = 45, message = "Alias must be 45 characters or less.")
    private String alias;

    @JsonIgnore
    @DecryptField
    private String number;

    @JsonIgnore
    private String username;

    @JsonIgnore
    private String channel;

    @JsonIgnore
    private String country;
}
