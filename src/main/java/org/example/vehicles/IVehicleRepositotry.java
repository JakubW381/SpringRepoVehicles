package org.example.vehicles;

import org.example.User.User;
import org.example.User.UserRepository;

import java.util.ArrayList;
import java.util.List;

public interface IVehicleRepositotry {

    void rentVehicle(String id, UserRepository userRepo);
    void returnVehicle(String id,UserRepository userRepo);
    void getVehicle();
    void save();
    void addVehicle(Vehicle veh,User user);
    void removeVehicle(String id,UserRepository userRepo);
}
