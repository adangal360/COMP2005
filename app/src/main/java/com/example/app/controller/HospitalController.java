package com.example.app.controller;

import com.example.app.service.HospitalService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class HospitalController {

    private final HospitalService hospitalService;

    public HospitalController(HospitalService hospitalService) {
        this.hospitalService = hospitalService;
    }

    @GetMapping("/f1/{patientId}")
    public List<Integer> getRoomsUsedByPatient(@PathVariable int patientId) {
        return hospitalService.getRoomsUsedByPatient(patientId);
    }
}
