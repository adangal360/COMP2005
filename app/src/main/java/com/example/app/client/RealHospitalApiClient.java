package com.example.app.client;

import com.example.app.model.Admission;
import com.example.app.model.Allocation;
import com.example.app.model.Employee;
import com.example.app.model.RoomAllocation;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.Arrays;
import java.util.List;

@Service
public class RealHospitalApiClient implements HospitalApiClient {

    private final RestClient restClient;

    public RealHospitalApiClient() {
        this.restClient = RestClient.builder()
                .baseUrl("https://web.socem.plymouth.ac.uk/COMP2005/api")
                .build();
    }

    @Override
    public List<Admission> getAdmissions() {
        Admission[] admissions = restClient.get()
                .uri("/Admissions")
                .retrieve()
                .body(Admission[].class);

        return Arrays.asList(admissions);
    }

    @Override
    public List<RoomAllocation> getRoomAllocations() {
        RoomAllocation[] roomAllocations = restClient.get()
                .uri("/RoomAllocations")
                .retrieve()
                .body(RoomAllocation[].class);

        return Arrays.asList(roomAllocations);
    }

    @Override
    public List<Allocation> getAllocations() {
        Allocation[] allocations = restClient.get()
                .uri("/Allocations")
                .retrieve()
                .body(Allocation[].class);

        return Arrays.asList(allocations);
    }

    @Override
    public List<Employee> getEmployees() {
        Employee[] employees = restClient.get()
                .uri("/Employees")
                .retrieve()
                .body(Employee[].class);

        return Arrays.asList(employees);
    }
}
