package org.example.repositories;

import org.example.models.User;
import org.example.models.Vehicle;

import java.util.List;
import java.util.Optional;

public interface VehicleRepository {
    void addVehicle(Vehicle veh, User user);

    List<Vehicle> findAll();
    Optional<Vehicle> findById(String id);
    Vehicle save(Vehicle vehicle);
    void deleteById(String id);


}
