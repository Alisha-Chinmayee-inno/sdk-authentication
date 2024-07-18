package com.locationguru.authentication.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.locationguru.authentication.model.Geofence;
import com.locationguru.authentication.model.Location;
import com.locationguru.authentication.repository.GeofenceRepository;
import com.locationguru.authentication.repository.LocationRepository;
import com.locationguru.authentication.request.SyncRequest;
import com.locationguru.authentication.response.GetDataResponse;

@Service
public class SyncService {

    private static final Logger logger = LoggerFactory.getLogger(SyncService.class);
    private final LocationRepository locationRepository;
    private final GeofenceRepository geofenceRepository;

    @Autowired
    public SyncService(LocationRepository locationRepository, GeofenceRepository geofenceRepository) {
        this.locationRepository = locationRepository;
        this.geofenceRepository = geofenceRepository;
    }

    @Transactional
    public void syncData(SyncRequest syncRequest) {
        logger.info("syncData called with request: {}", syncRequest);

        if (syncRequest.getLocations() != null) {
            syncRequest.getLocations().forEach(location -> {
                if (location != null) {
                    locationRepository.save(location);
                }
            });
        }
        if (syncRequest.getGeofences() != null) {
            syncRequest.getGeofences().forEach(geofence -> {
                if (geofence != null) {
                    geofenceRepository.save(geofence);
                }
            });
        }
    }

    public GetDataResponse getData(Integer historyCount) {
        logger.info("getData called with historycount: {}", historyCount);

        GetDataResponse response = new GetDataResponse();
        Pageable pageable = PageRequest.of(0, historyCount != null && historyCount > 0 ? historyCount : 2000);

        List<Location> locations = historyCount != null && historyCount > 0 
                ? locationRepository.findRecentEntriesWithHistoryCount(historyCount, pageable) 
                : locationRepository.findRecentEntries(pageable);
        List<Geofence> geofences = historyCount != null && historyCount > 0 
                ? geofenceRepository.findRecentEntriesWithHistoryCount(historyCount, pageable) 
                : geofenceRepository.findRecentEntries(pageable);

        response.setLocations(locations);
        response.setGeofences(geofences);

        return response;
    }
}