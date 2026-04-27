package com.example.app.service;

import com.example.app.client.HospitalApiClient;
import com.example.app.model.Admission;
import com.example.app.model.RoomAllocation;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HospitalService {

    private final HospitalApiClient hospitalApiClient;

    public HospitalService(HospitalApiClient hospitalApiClient) {
        this.hospitalApiClient = hospitalApiClient;
    }

    public List<Integer> getRoomsUsedByPatient(int patientId) {
        List<Integer> admissionIds = hospitalApiClient.getAdmissions()
                .stream()
                .filter(admission -> admission.getPatientID() == patientId)
                .map(Admission::getId)
                .toList();

        return hospitalApiClient.getRoomAllocations()
                .stream()
                .filter(roomAllocation -> admissionIds.contains(roomAllocation.getAdmissionID()))
                .map(RoomAllocation::getRoomID)
                .distinct()
                .toList();
    }
}
