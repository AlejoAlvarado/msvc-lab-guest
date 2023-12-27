package com.sunset.rider.msvclabguest.repository;

import com.sunset.rider.msvclabguest.model.GuestType;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GuestTypeRepository extends ReactiveMongoRepository<GuestType,String> {
}
