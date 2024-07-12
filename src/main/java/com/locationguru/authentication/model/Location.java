package com.locationguru.authentication.model;

import java.time.Instant;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;


@Entity
@Table(name = "locations")
public class Location {

    @Id
    @Column(name = "id")
    private String id; 
    
    @Column(name = "lat")
    private double lat;

    @Column(name = "lon")
    private double lon;

    private long timestamp;

    public Location() {
        this.id = String.format("%04x", new java.util.Random().nextInt(0x10000));
        this.timestamp = Instant.now().toEpochMilli();
    }

    public Location(double lat, double lon, long timestamp) {
        this.id = generateId();
        this.lat = lat;
        this.lon = lon;
        this.timestamp = timestamp;
    }

    private String generateId() {
        String uuid = UUID.randomUUID().toString();
        return uuid.substring(0, 4);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}