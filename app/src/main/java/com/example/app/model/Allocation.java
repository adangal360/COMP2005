package com.example.app.model;

public class Allocation {
    private int id;
    private int admissionID;
    private int employeeID;
    private int roomID;
    private String startTime;
    private String endTime;

    public Allocation(int id, int admissionID, int employeeID, int roomID, String startTime, String endTime) {
        this.id = id;
        this.admissionID = admissionID;
        this.employeeID = employeeID;
        this.roomID = roomID;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public int getEmployeeID() {
        return employeeID;
    }

    public String getStartTime() {
        return startTime;
    }

    public String getEndTime() {
        return endTime;
    }
}