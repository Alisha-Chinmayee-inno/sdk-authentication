package com.locationguru.authentication.response;

import com.locationguru.authentication.model.Geofence;
import com.locationguru.authentication.model.Location;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class GetDataResponse {
    private int status;
    private String message;
    private List<Location> locations;
    private List<Geofence> geofences;

    public GetDataResponse() {
    }

    public GetDataResponse(int status, String message, List<Location> locations, List<Geofence> geofences) {
        this.status = status;
        this.message = message;
        this.locations = locations;
        this.geofences = geofences;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

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
