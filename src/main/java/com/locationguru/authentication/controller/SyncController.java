package com.locationguru.authentication.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.config.annotation.CorsRegistry;

import com.locationguru.authentication.request.SyncRequest;
import com.locationguru.authentication.response.GetDataResponse;
import com.locationguru.authentication.response.SyncResponse;
import com.locationguru.authentication.service.SyncService;

@CrossOrigin
@RestController
@RequestMapping("/api/sync")
public class SyncController {

    private final SyncService syncService;

    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
            .allowedOrigins("http://geolocation-app.s3-website.ap-south-1.amazonaws.com/login")
            .allowedMethods("GET", "POST", "PUT", "DELETE", "HEAD", "OPTIONS")
            .allowedHeaders("*")
            .allowCredentials(true);
    }

    @Autowired
    public SyncController(SyncService syncService) {
        this.syncService = syncService;
    }
    
    @PostMapping("/history")
    public ResponseEntity<SyncResponse> syncData(@RequestBody SyncRequest syncRequest) {
        if (syncRequest == null || (syncRequest.getLocations() == null && syncRequest.getGeofences() == null)) {
            throw new IllegalArgumentException("SyncRequest body must not be null and must contain locations or geofences");
        }

        if ((syncRequest.getLocations() == null || syncRequest.getLocations().size() == 0) && (syncRequest.getGeofences() == null || syncRequest.getGeofences().size() == 0)) {
            throw new IllegalArgumentException("SyncRequest must contain non-empty locations or geofences");
        }

        syncService.syncData(syncRequest);

        // Return 200 OK with specific JSON body
        SyncResponse responseBody = new SyncResponse(200, "");
        return ResponseEntity.ok(responseBody);
    }


    @GetMapping("/history")
    public ResponseEntity<GetDataResponse> getData(@RequestParam(value = "historycount", required = false) Integer historyCount) {
        try {
            if (historyCount != null && historyCount <= 0) {
                throw new IllegalArgumentException("historycount must be greater than 0");
            }

            GetDataResponse response = syncService.getData(historyCount);
            response.setStatus(200);
            response.setMessage("");
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException ex) {
            GetDataResponse errorResponse = new GetDataResponse();
            errorResponse.setStatus(400); 
            errorResponse.setMessage(ex.getMessage());
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }

}
