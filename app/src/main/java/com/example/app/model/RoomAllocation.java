package com.example.app.model;

public class RoomAllocation {
    private int id;
    private int admissionID;
    private int roomID;
    private String timeIn;
    private String timeOut;

    public int getId() {
        return id;
    }

    public int getAdmissionID() {
        return admissionID;
    }

    public int getRoomID() {
        return roomID;
    }

    public String getTimeIn() {
        return timeIn;
    }

    public String getTimeOut() {
        return timeOut;
    }

    public RoomAllocation(int id, int admissionID, int roomID, String timeIn, String timeOut) {
        this.id = id;
        this.admissionID = admissionID;
        this.roomID = roomID;
        this.timeIn = timeIn;
        this.timeOut = timeOut;
    }
}
