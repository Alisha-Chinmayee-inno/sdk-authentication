package com.locationguru.authentication.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.locationguru.authentication.model.Geofence;

import java.util.List;

public interface GeofenceRepository extends JpaRepository<Geofence, String> {

    @Query("SELECT geo FROM Geofence geo ORDER BY geo.timestamp DESC")
    List<Geofence> findRecentEntries(Pageable pageable);

    @Query("SELECT geo FROM Geofence geo ORDER BY geo.timestamp DESC")
    List<Geofence> findRecentEntriesWithHistoryCount(@Param("historyCount") int historyCount, Pageable pageable);
}