package com.sunset.rider.msvclabguest.repository;

import com.sunset.rider.msvclabguest.model.Guest;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GuestRepository extends ReactiveMongoRepository<Guest,String> {
}
