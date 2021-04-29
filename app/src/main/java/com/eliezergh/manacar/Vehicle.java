package com.eliezergh.manacar;

public class Vehicle {
    protected String vehicleName;
    protected String Motor;
    protected String vehicleRegistrationNumber;

    public String getMotor() {
        return Motor;
    }

    public String getVehicleName() {
        return vehicleName;
    }

    public String getVehicleRegistrationNumber() {
        return vehicleRegistrationNumber;
    }

    public void setMotor(String motor) {
        this.Motor = motor;
    }

    public void setVehicleName(String vehicleName) {
        this.vehicleName = vehicleName;
    }

    public void setVehicleRegistrationNumber(String vehicleRegistrationNumber) {
        this.vehicleRegistrationNumber = vehicleRegistrationNumber;
    }
}