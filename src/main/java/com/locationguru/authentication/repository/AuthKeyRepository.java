package com.locationguru.authentication.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.locationguru.authentication.model.AuthKey;

@Repository
public interface AuthKeyRepository extends JpaRepository<AuthKey, String> {

    @Query("SELECT ak FROM AuthKey ak WHERE ak.key = :key")
    AuthKey findByKey(@Param("key") String key);
    
    @Query("SELECT ak FROM AuthKey ak WHERE ak.organisationName = :organisationName")
    AuthKey findByOrganisationName(@Param("organisationName") String organisationName);
}
