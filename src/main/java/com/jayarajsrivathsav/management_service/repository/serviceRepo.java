package com.jayarajsrivathsav.management_service.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import com.jayarajsrivathsav.management_service.model.Service;

import java.util.List;



public interface serviceRepo extends MongoRepository<Service, String> {

    @Query("{ $text: { $search: ?0 } }")
    static
    
    List<Service> findByKeyword(String search) {
    
        throw new UnsupportedOperationException("Unimplemented method 'findBySearchTerm'");
    }
}