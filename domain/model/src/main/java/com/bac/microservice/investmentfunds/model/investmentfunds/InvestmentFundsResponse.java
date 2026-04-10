package com.bac.microservice.investmentfunds.model.investmentfunds;

import lombok.Getter;
import lombok.Setter;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@Builder
public class InvestmentFundsResponse {

    private Message message;

    @Getter
    @Setter
    @Builder
    public static class Message implements Serializable {
        private Header header;
        private Key key;
        private Body body;

        @Getter
        @Setter
        @Builder
        public static class Header implements Serializable {
            private String operationCode;
            private Origin origin;
            private Target target;

            @Getter
            @Setter
            @Builder
            public static class Origin implements Serializable {
                private String country;
                private String channel;
                private String user;
                private String server;
            }

            @Getter
            @Setter
            @Builder
            public static class Target implements Serializable {
                private String country;
                private String channel;
            }

            @Getter
            @Setter
            @Builder
            @NoArgsConstructor
            @AllArgsConstructor
            public static class Errors implements Serializable {
                private List<Error> error;

                @Getter
                @Setter
                @Builder
                @NoArgsConstructor
                @AllArgsConstructor
                public static class Error implements Serializable {
                    private String code;
                    private String description;
                }
            }
        }

        @Getter
        @Setter
        @Builder
        public static class Key implements Serializable {
            private String fundCode;
            private String cifCode;
            private String customerCode;
            private String activeCustomer;
        }

        @Getter
        @Setter
        @Builder
        public static class Body implements Serializable {
            private CustomerList customerList;

            @Getter
            @Setter
            @Builder
            public static class CustomerList implements Serializable {
                private List<CustomerFund> customerFund;

                @Getter
                @Setter
                @Builder
                public static class CustomerFund implements Serializable {
                    private String customerCode;
                    private List<Fund> fund;

                    @Getter
                    @Setter
                    @Builder
                    public static class Fund implements Serializable {
                        private String cutOffDate;
                        private String cifCode;
                        private String customerCode;
                        private String customerName;
                        private String portfolioCode;
                        private String fundCode;
                        private String fundName;
                        private String localcurrencyCode;
                        private String currencyCode;
                        private String participationQuantity;
                        private String participationPrice;
                        private String balance;
                        private String suggestedDays;
                        private String currentDays;
                        private String firstInvestmentDate;
                        private String transitFunds;
                        private String minimumAmount;
                        private String investmentTime;
                        private String destinationAccount;
                        private String withdrawalTime;
                        private String minWithdrawalAmount;
                        private String foundType;
                        private String clientProfile;
                        private String clientActive;
                        private String minimumAmountOpening;
                        private String yieldRate;
                        private String numberYieldRateDays;
                        private String expiredProfile;
                        private String profileExpirationDate;
                        private String allowsInvest;
                        private String allowsWithdrawal;
                    }
                }
            }
        }
    }
}
