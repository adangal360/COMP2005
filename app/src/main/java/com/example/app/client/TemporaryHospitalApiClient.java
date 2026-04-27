package com.example.app.client;

import com.example.app.model.Admission;
import com.example.app.model.Allocation;
import com.example.app.model.Employee;
import com.example.app.model.RoomAllocation;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TemporaryHospitalApiClient implements HospitalApiClient {

    @Override
    public List<Admission> getAdmissions() {
        return List.of();
    }

    @Override
    public List<RoomAllocation> getRoomAllocations() {
        return List.of();
    }
    @Override
    public List<Allocation> getAllocations() {
        return List.of();
    }

    @Override
    public List<Employee> getEmployees() {
        return List.of();
    }
}
