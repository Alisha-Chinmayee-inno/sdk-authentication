package com.locationguru.authentication.request;

import com.locationguru.authentication.model.Geofence;
import com.locationguru.authentication.model.Location;

import java.util.ArrayList;
import java.util.List;

public class SyncRequest {
    private List<Location> locations = new ArrayList<>();
    private List<Geofence> geofences = new ArrayList<>();

    public List<Location> getLocations() {
        return locations;
    }

    public void setLocations(List<Location> locations) {
        this.locations = locations;
    }

    public List<Geofence> getGeofences() {
        return geofences;
    }

    public void setGeofences(List<Geofence> geofences) {
        this.geofences = geofences;
    }
}
