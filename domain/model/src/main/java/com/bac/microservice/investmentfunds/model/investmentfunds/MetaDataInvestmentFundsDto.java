package com.bac.microservice.investmentfunds.model.investmentfunds;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MetaDataInvestmentFundsDto {

    private Boolean hasInvestmentFunds;

}
