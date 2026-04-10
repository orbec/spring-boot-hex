package com.bac.microservice.investmentfunds.model.alias;

import com.bac.lib.aliasmanager.enums.ProductType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ProductAlias {

    private String username;
    private String typeCd;
    private String countryCd;
    private String cifCd;
    private String productCd;
    private String productNumber;
    private String channel;
    private String alias;
    private String bankId;
    private ProductType productType;
    private String conversionCode;
    private Boolean isActive;

}
