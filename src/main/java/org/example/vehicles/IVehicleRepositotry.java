package org.example.vehicles;

import java.util.ArrayList;
import java.util.List;

public interface IVehicleRepositotry {

    List<Vehicle> vehicles = new ArrayList<>();

    void rentVehicle(int id);
    void returnVehicle(int id);
    void getVehicle();
    void save();
}
