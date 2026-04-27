package com.example.app.model;

public class Admission {
    private int id;
    private String admissionDate;
    private String dischargeDate;
    private int patientID;

    public int getId() {
        return id;
    }

    public String getAdmissionDate() {
        return admissionDate;
    }

    public String getDischargeDate() {
        return dischargeDate;
    }

    public int getPatientID() {
        return patientID;
    }

    public Admission(int id, String admissionDate, String dischargeDate, int patientID) {
        this.id = id;
        this.admissionDate = admissionDate;
        this.dischargeDate = dischargeDate;
        this.patientID = patientID;
    }
}
