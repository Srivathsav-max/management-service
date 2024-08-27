package com.jayarajsrivathsav.management_service.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.jayarajsrivathsav.management_service.model.Service;

import java.util.Optional;

public interface serviceRepo extends MongoRepository<Service, String> {

    static Optional<Service> findByUniqueKey(String nodeId) {
        throw new UnsupportedOperationException("Unimplemented method 'findByUniqueKey'");
    }
}
