package org.example.repositories;

import org.example.models.User;
import org.example.models.Vehicle;

import java.util.List;
import java.util.Optional;

public interface VehicleRepositotry {

    //void rentVehicle(String id, UserRepository userRepo);
    //void returnVehicle(String id,UserRepository userRepo);
    //void getVehicle();
    //void save();
    void addVehicle(Vehicle veh, User user);
    //void removeVehicle(String id,UserRepository userRepo);

    List<Vehicle> findAll();
    Optional<Vehicle> findById(String id);
    Vehicle save(Vehicle vehicle);
    void deleteById(String id);


}
