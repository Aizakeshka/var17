package com.example.var17;

public class Pvz {
    private String addressDivision;
    private String address;
    private double latitude;
    private double longitude;
    private boolean isWarehouseAcceptsFreights;
    private boolean isWarehouseGivesFreights;

    public String getCity() {
        return addressDivision;
    }

    public String getAddress() {
        return address;
    }

    public String getCoordinates() {
        return latitude + ", " + longitude;
    }

    public String getType() {
        if (isWarehouseAcceptsFreights && isWarehouseGivesFreights) return "Приём, Выдача";
        if (isWarehouseAcceptsFreights) return "Приём";
        if (isWarehouseGivesFreights) return "Выдача";
        return "Неизвестно";
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }
}

