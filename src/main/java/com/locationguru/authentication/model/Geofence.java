package com.locationguru.authentication.model;

import java.time.Instant;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "geofences")
public class Geofence {

    @Id
    @Column(name = "id")
    private String id; 

    @Column(name = "lat")
    private double lat;

    @Column(name = "lon")
    private double lon;

    @Column(name = "radius")
    private int radius;

    @Column(name = "fence_name")
    private String fenceName;

    @Column(name = "timestamp")
    private long timestamp;

    @Column(name = "event_type")
    private int eventType;

    public Geofence() {
        this.id = String.format("%04x", new java.util.Random().nextInt(0x10000));
        this.timestamp = Instant.now().toEpochMilli();
    }

    public Geofence(double lat, double lon, int radius, String fenceName, long timestamp, int eventType) {
        this.id = generateId();
        this.lat = lat;
        this.lon = lon;
        this.radius = radius;
        this.fenceName = fenceName;
        this.timestamp = timestamp;
        this.eventType = eventType;
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

    public int getRadius() {
        return radius;
    }

    public void setRadius(int radius) {
        this.radius = radius;
    }

    public String getFenceName() {
        return fenceName;
    }

    public void setFenceName(String fenceName) {
        this.fenceName = fenceName;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public int getEventType() {
        return eventType;
    }

    public void setEventType(int eventType) {
        this.eventType = eventType;
    }
}