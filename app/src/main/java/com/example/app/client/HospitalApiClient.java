package com.example.app.client;

import com.example.app.model.Admission;
import com.example.app.model.Allocation;
import com.example.app.model.Employee;
import com.example.app.model.RoomAllocation;

import java.util.List;

public interface HospitalApiClient {
    List<Admission> getAdmissions();
    List<RoomAllocation> getRoomAllocations();
    List<Allocation> getAllocations();
    List<Employee> getEmployees();
}
