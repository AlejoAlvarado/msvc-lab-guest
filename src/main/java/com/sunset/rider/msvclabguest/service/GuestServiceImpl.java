package com.sunset.rider.msvclabguest.service;

import com.sunset.rider.msvclabguest.model.Guest;
import com.sunset.rider.msvclabguest.repository.GuestRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class GuestServiceImpl implements GuestService{

    private GuestRepository guestRepository;

    public GuestServiceImpl(GuestRepository guestRepository){
        this.guestRepository = guestRepository;
    }

    @Override
    public Flux<Guest> findAll() {
        return guestRepository.findAll();
    }

    @Override
    public Mono<Guest> findById(String id) {
        return guestRepository.findById(id);
    }

    @Override
    public Mono<Guest> save(Guest guest) {
        return guestRepository.save(guest);
    }

    @Override
    public Mono<Guest> update(Guest guest) {
        return guestRepository.save(guest);
    }

    @Override
    public Mono<Void> delete(String id) {
        return guestRepository.deleteById(id);
    }
}
