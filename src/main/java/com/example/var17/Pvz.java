package com.example.var17;

public class Pvz {
    private final String city;
    private final String address;
    private final String type;

    public Pvz(PvzJson pvzJson) {
        this.city = pvzJson.addressDivision;
        this.address = pvzJson.address;

        if (pvzJson.isWarehouseAcceptsFreights && pvzJson.isWarehouseGivesFreights) {
            this.type = "Оба";
        } else if (pvzJson.isWarehouseAcceptsFreights) {
            this.type = "Приём";
        } else if (pvzJson.isWarehouseGivesFreights) {
            this.type = "Выдача";
        } else {
            this.type = "Неизвестно";
        }
    }

    public String getCity() { return city; }
    public String getAddress() { return address; }
    public String getType() { return type; }
}