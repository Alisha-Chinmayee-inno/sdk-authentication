package com.locationguru.authentication.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Pageable;

import com.locationguru.authentication.model.Geofence;
import com.locationguru.authentication.model.Location;
import com.locationguru.authentication.repository.GeofenceRepository;
import com.locationguru.authentication.repository.LocationRepository;
import com.locationguru.authentication.request.SyncRequest;
import com.locationguru.authentication.response.GetDataResponse;

@ExtendWith(MockitoExtension.class)
public class SyncServiceTest {

    @Mock
    private LocationRepository locationRepository;

    @Mock
    private GeofenceRepository geofenceRepository;

    @InjectMocks
    private SyncService syncService;

    private SyncRequest syncRequest;

    @BeforeEach
    public void setup() {
        Location location = new Location();
        Geofence geofence = new Geofence();

        syncRequest = new SyncRequest();
        syncRequest.setLocations(Collections.singletonList(location));
        syncRequest.setGeofences(Collections.singletonList(geofence));
    }

    @Test
    public void testSyncData() {
        syncService.syncData(syncRequest);

        verify(locationRepository, times(1)).save(any(Location.class));
        verify(geofenceRepository, times(1)).save(any(Geofence.class));
    }

    @Test
    public void testSyncDataWithNullLocationsAndGeofences() {
        SyncRequest nullSyncRequest = new SyncRequest();
        nullSyncRequest.setLocations(null);
        nullSyncRequest.setGeofences(null);

        syncService.syncData(nullSyncRequest);

        verify(locationRepository, never()).save(any(Location.class));
        verify(geofenceRepository, never()).save(any(Geofence.class));
    }

    @Test
    public void testSyncDataWithEmptyLists() {
        SyncRequest emptySyncRequest = new SyncRequest();
        emptySyncRequest.setLocations(Collections.emptyList());
        emptySyncRequest.setGeofences(Collections.emptyList());

        syncService.syncData(emptySyncRequest);

        verify(locationRepository, never()).save(any(Location.class));
        verify(geofenceRepository, never()).save(any(Geofence.class));
    }

    @Test
    public void testGetDataWithHistoryCount() {
        List<Location> mockLocations = Arrays.asList(new Location(), new Location());
        List<Geofence> mockGeofences = Arrays.asList(new Geofence(), new Geofence());
        when(locationRepository.findRecentEntriesWithHistoryCount(anyInt(), any(Pageable.class))).thenReturn(mockLocations);
        when(geofenceRepository.findRecentEntriesWithHistoryCount(anyInt(), any(Pageable.class))).thenReturn(mockGeofences);

        GetDataResponse response = syncService.getData(2);

        verify(locationRepository, times(1)).findRecentEntriesWithHistoryCount(anyInt(), any(Pageable.class));
        verify(geofenceRepository, times(1)).findRecentEntriesWithHistoryCount(anyInt(), any(Pageable.class));

        assert response.getLocations().size() == 2;
        assert response.getGeofences().size() == 2;
    }

    @Test
    public void testGetDataWithHistoryCountZero() {
        List<Location> mockLocations = Arrays.asList(new Location(), new Location());
        List<Geofence> mockGeofences = Arrays.asList(new Geofence(), new Geofence());
        when(locationRepository.findRecentEntries(any(Pageable.class))).thenReturn(mockLocations);
        when(geofenceRepository.findRecentEntries(any(Pageable.class))).thenReturn(mockGeofences);

        GetDataResponse response = syncService.getData(0);

        verify(locationRepository, times(1)).findRecentEntries(any(Pageable.class));
        verify(geofenceRepository, times(1)).findRecentEntries(any(Pageable.class));

        assert response.getLocations().size() == 2;
        assert response.getGeofences().size() == 2;
    }

    @Test
    public void testGetDataWithEmptyResults() {
        when(locationRepository.findRecentEntries(any(Pageable.class))).thenReturn(Collections.emptyList());
        when(geofenceRepository.findRecentEntries(any(Pageable.class))).thenReturn(Collections.emptyList());

        GetDataResponse response = syncService.getData(null);

        verify(locationRepository, times(1)).findRecentEntries(any(Pageable.class));
        verify(geofenceRepository, times(1)).findRecentEntries(any(Pageable.class));

        assert response.getLocations().isEmpty();
        assert response.getGeofences().isEmpty();
    }

    @Test
    public void testGetDataWithDefault() {
        List<Location> mockLocations = Arrays.asList(new Location(), new Location());
        List<Geofence> mockGeofences = Arrays.asList(new Geofence(), new Geofence());
        when(locationRepository.findRecentEntries(any(Pageable.class))).thenReturn(mockLocations);
        when(geofenceRepository.findRecentEntries(any(Pageable.class))).thenReturn(mockGeofences);

        GetDataResponse response = syncService.getData(null);

        verify(locationRepository, times(1)).findRecentEntries(any(Pageable.class));
        verify(geofenceRepository, times(1)).findRecentEntries(any(Pageable.class));

        assert response.getLocations().size() == 2;
        assert response.getGeofences().size() == 2;
    }
}
