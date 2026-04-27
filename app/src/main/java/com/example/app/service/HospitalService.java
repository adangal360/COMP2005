package com.example.app.service;

import com.example.app.client.HospitalApiClient;
import com.example.app.model.Admission;
import com.example.app.model.RoomAllocation;
import org.springframework.stereotype.Service;

import java.util.List;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.stream.Collectors;

import com.example.app.model.Allocation;
import com.example.app.model.Employee;
import java.util.ArrayList;

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

    public List<Integer> getPatientsInRoomLast7Days(int roomId) {

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime sevenDaysAgo = now.minusDays(7);

        DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE_TIME;

        // Step 1: get admissions in this room within last 7 days
        List<Integer> admissionIds = hospitalApiClient.getRoomAllocations()
                .stream()
                .filter(ra -> ra.getRoomID() == roomId)
                .filter(ra -> {
                    LocalDateTime timeIn = LocalDateTime.parse(ra.getTimeIn(), formatter);
                    return timeIn.isAfter(sevenDaysAgo);
                })
                .map(RoomAllocation::getAdmissionID)
                .toList();

        // Step 2: map admissions to patients
        return hospitalApiClient.getAdmissions()
                .stream()
                .filter(a -> admissionIds.contains(a.getId()))
                .map(Admission::getPatientID)
                .distinct()
                .toList();
    }

    public Integer getLeastUsedRoom() {
        return hospitalApiClient.getRoomAllocations()
                .stream()
                .collect(Collectors.groupingBy(
                        RoomAllocation::getRoomID,
                        Collectors.counting()
                ))
                .entrySet()
                .stream()
                .min(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse(null);
    }

    public List<Integer> getStaffResponsibleForThreeOrMorePatientsConcurrently() {
        List<Integer> result = new ArrayList<>();

        for (Employee employee : hospitalApiClient.getEmployees()) {
            List<Allocation> employeeAllocations = hospitalApiClient.getAllocations()
                    .stream()
                    .filter(allocation -> allocation.getEmployeeID() == employee.getId())
                    .toList();

            boolean hasThreeConcurrentPatients = employeeAllocations
                    .stream()
                    .anyMatch(current -> countOverlappingAllocations(current, employeeAllocations) >= 3);

            if (hasThreeConcurrentPatients) {
                result.add(employee.getId());
            }
        }

        return result;
    }

    private int countOverlappingAllocations(Allocation current, List<Allocation> allocations) {
        return (int) allocations.stream()
                .filter(other -> allocationsOverlap(current, other))
                .count();
    }

    private boolean allocationsOverlap(Allocation a, Allocation b) {
        LocalDateTime aStart = LocalDateTime.parse(a.getStartTime());
        LocalDateTime aEnd = parseEndTimeOrNow(a.getEndTime());

        LocalDateTime bStart = LocalDateTime.parse(b.getStartTime());
        LocalDateTime bEnd = parseEndTimeOrNow(b.getEndTime());

        return !aStart.isAfter(bEnd) && !bStart.isAfter(aEnd);
    }

    private LocalDateTime parseEndTimeOrNow(String endTime) {
        if (endTime == null || endTime.isBlank()) {
            return LocalDateTime.now();
        }

        return LocalDateTime.parse(endTime);
    }
}
