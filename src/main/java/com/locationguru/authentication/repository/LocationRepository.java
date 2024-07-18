package com.locationguru.authentication.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.locationguru.authentication.model.Location;

import java.util.List;

public interface LocationRepository extends JpaRepository<Location, String> {

    @Query("SELECT loc FROM Location loc ORDER BY loc.timestamp DESC")
    List<Location> findRecentEntries(Pageable pageable);

    @Query("SELECT loc FROM Location loc ORDER BY loc.timestamp DESC")
    List<Location> findRecentEntriesWithHistoryCount(@Param("historyCount") int historyCount, Pageable pageable);
}