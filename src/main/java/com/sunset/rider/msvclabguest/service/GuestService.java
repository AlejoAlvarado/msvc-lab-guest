package com.sunset.rider.msvclabguest.service;

import com.sunset.rider.msvclabguest.model.Guest;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface GuestService {
    Flux<Guest> findAll();

    Mono<Guest> findById(String id);

    Mono<Guest> save(Guest guest);

    Mono<Guest> update(Guest guest);

    Mono<Void> delete(String id);
}
