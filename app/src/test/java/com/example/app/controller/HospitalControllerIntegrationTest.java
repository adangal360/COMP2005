package com.example.app.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.example.app.service.HospitalService;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;

import static org.mockito.Mockito.when;

@WebMvcTest(HospitalController.class)
class HospitalControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private HospitalService hospitalService;

    @Test
    void getRoomsUsedByPatientEndpointReturnsRoomsAsJson() throws Exception {
        // Arrange
        when(hospitalService.getRoomsUsedByPatient(10))
                .thenReturn(List.of(1, 2));

        // Act + Assert
        mockMvc.perform(get("/api/f1/10"))
                .andExpect(status().isOk())
                .andExpect(content().json("[1,2]"));
    }
}