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

    @GetMapping("/f2/{roomId}")
    public List<Integer> getPatientsInRoomLast7Days(@PathVariable int roomId) {
        return hospitalService.getPatientsInRoomLast7Days(roomId);
    }

    @GetMapping("/f3")
    public Integer getLeastUsedRoom() {
        return hospitalService.getLeastUsedRoom();
    }

    @GetMapping("/f4")
    public List<Integer> getStaffResponsibleForThreeOrMorePatientsConcurrently() {
        return hospitalService.getStaffResponsibleForThreeOrMorePatientsConcurrently();
    }
}
