package com.bac.microservice.investmentfunds.model.port;

import org.reactivestreams.Publisher;

public interface UseCase<R, T> {
    Publisher<T> execute(R request);
}
