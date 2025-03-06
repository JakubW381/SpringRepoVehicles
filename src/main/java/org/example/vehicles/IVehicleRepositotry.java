package org.example.vehicles;

import org.example.User.User;
import org.example.User.UserRepository;

import java.util.ArrayList;
import java.util.List;

public interface IVehicleRepositotry {

    void rentVehicle(int id, UserRepository userRepo);
    void returnVehicle(int id,UserRepository userRepo);
    void getVehicle();
    void save();
    void addVehicle(Vehicle veh,User user);
    void removeVehicle(int id,UserRepository userRepo);
}
