package com.bac.microservice.investmentfunds.model.investmentfunds;

import com.bac.lib.secure.crypto.domain.crypto.annotation.EncryptField;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InvestmentFundsDto implements Serializable {

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Result data;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<Errors> errors;

    @Data
    @Getter
    @Setter
    public static class Result implements Serializable {
        @JsonInclude(JsonInclude.Include.NON_NULL)
        private List<Fund> investmentFunds;

        @JsonInclude(JsonInclude.Include.NON_NULL)
        private BalanceSummary balanceSummary;

        @Data
        @Getter
        @Setter
        public static class Fund implements Serializable {
            private String alias;
            @EncryptField
            private String number;
            private BigDecimal localBalance;
            private BigDecimal availableBalance;
            private String localCurrency;
            private String country;
            @JsonIgnore
            private String cif;
            @JsonIgnore
            private String conversionCode;
        }

        @Data
        @Getter
        @Setter
        public static class BalanceSummary implements Serializable {
            private BigDecimal localBalanceTotal;
            private BigDecimal localAvailableBalanceTotal;
            private BigDecimal dollarBalanceTotal;
            private BigDecimal dollarAvailableBalanceTotal;
            private String localCurrencySummary;
            private Integer size;
        }
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class Errors implements Serializable {
        private Error error;

        @Getter
        @Setter
        @Builder
        @NoArgsConstructor
        @AllArgsConstructor
        @JsonInclude(JsonInclude.Include.NON_NULL)
        public static class Error implements Serializable {
            private String code;
            private String description;
        }

    }

}
