package com.bac.microservice.investmentfunds.model.investmentfunds;

import lombok.Getter;
import lombok.Setter;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.io.Serializable;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MessageRequest {

    private Message message;

    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Message implements Serializable {
        private Header header;
        private Key key;

        @Getter
        @Setter
        @Builder
        @AllArgsConstructor
        @NoArgsConstructor
        public static class Key implements Serializable {
            private String fundCode;
            private String cifCode;
            private String customerCode;
            private String activeCustomer;
        }

        @Getter
        @Setter
        @Builder
        @AllArgsConstructor
        @NoArgsConstructor
        public static class Header implements Serializable {
            private String operationCode;
            private Origin origin;
            private Target target;

            @Getter
            @Setter
            @Builder
            @AllArgsConstructor
            @NoArgsConstructor
            public static class Origin implements Serializable {
                private String country;
                private String channel;
                private String user;
                private String server;
            }

            @Getter
            @Setter
            @Builder
            @AllArgsConstructor
            @NoArgsConstructor
            public static class Target implements Serializable {
                private String country;
                private String channel;
            }

        }

    }

}
