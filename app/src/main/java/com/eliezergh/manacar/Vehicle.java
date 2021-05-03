package com.eliezergh.manacar;

public class Vehicle {
    protected String vehicleManufacturer;
    protected String Motor;
    protected String vehicleRegistrationNumber;
    protected String vehicleMainImage;

    public Vehicle() {

    }
    public Vehicle(String vehicleManufacturer, String Motor, String vehicleRegistrationNumber, String vehicleMainImage){
        this.vehicleManufacturer = vehicleManufacturer;
        this.Motor = Motor;
        this.vehicleRegistrationNumber = vehicleRegistrationNumber;
        this.vehicleMainImage = vehicleMainImage;
    }

    public String getMotor() {
        return Motor;
    }

    public String getVehicleManufacturer() {
        return vehicleManufacturer;
    }

    public String getVehicleRegistrationNumber() {
        return vehicleRegistrationNumber;
    }

    public String getVehicleMainImage(){ return vehicleMainImage;}

    public void setMotor(String motor) {
        this.Motor = motor;
    }

    public void setVehicleName(String vehicleName) {
        this.vehicleManufacturer = vehicleName;
    }

    public void setVehicleRegistrationNumber(String vehicleRegistrationNumber) {
        this.vehicleRegistrationNumber = vehicleRegistrationNumber;
    }

    public void setVehicleMainImage(String vehicleMainImage) {
        this.vehicleMainImage = vehicleMainImage;
    }
}