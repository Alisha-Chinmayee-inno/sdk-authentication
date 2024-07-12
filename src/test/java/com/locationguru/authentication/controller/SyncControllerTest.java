package com.locationguru.authentication.controller;

import com.locationguru.authentication.request.SyncRequest;
import com.locationguru.authentication.response.GetDataResponse;
import com.locationguru.authentication.service.SyncService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Collections;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class SyncControllerTest {

    @Mock
    private SyncService syncService;

    @InjectMocks
    private SyncController syncController;

    private MockMvc mockMvc;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(syncController).build();
    }

    @Test
    public void testSyncData_Success() throws Exception {
        SyncRequest syncRequest = new SyncRequest();
        syncRequest.setLocations(Collections.emptyList());
        syncRequest.setGeofences(Collections.emptyList());

        mockMvc.perform(post("/api/sync/history")
                .contentType("application/json")
                .content("{ \"locations\": [], \"geofences\": [] }"))
                .andExpect(status().isOk());
    }

    @Test
    public void testSyncData_BadRequest_NullBody() throws Exception {
        mockMvc.perform(post("/api/sync/history")
                .contentType("application/json"))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testSyncData_BadRequest_NullLocationsAndGeofences() throws Exception {
        mockMvc.perform(post("/api/sync/history")
                .contentType("application/json")
                .content("{ \"locations\": null, \"geofences\": null }"))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testGetData_Success_NoHistoryCount() throws Exception {
        GetDataResponse getDataResponse = new GetDataResponse();
        when(syncService.getData(null)).thenReturn(getDataResponse);

        mockMvc.perform(get("/api/sync/history"))
                .andExpect(status().isOk());
    }

    @Test
    public void testGetData_Success_WithHistoryCount() throws Exception {
        GetDataResponse getDataResponse = new GetDataResponse();
        when(syncService.getData(10)).thenReturn(getDataResponse);

        mockMvc.perform(get("/api/sync/history")
                .param("historycount", "10"))
                .andExpect(status().isOk());
    }

    @Test
    public void testGetData_BadRequest_InvalidHistoryCount() throws Exception {
        mockMvc.perform(get("/api/sync/history")
                .param("historycount", "0"))
                .andExpect(status().isBadRequest());
    }
}
