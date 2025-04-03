package org.example.app;

import org.example.repositories.RentalRepository;
import org.example.repositories.UserRepository;
import org.example.repositories.VehicleRepository;
import org.example.repositories.implementations.jdbc.RentalJdbcRepository;
import org.example.repositories.implementations.jdbc.UserJdbcRepository;
import org.example.repositories.implementations.jdbc.VehicleJdbcRepository;
import org.example.repositories.implementations.json.RentalJsonRepository;
import org.example.repositories.implementations.json.UserJsonRepository;
import org.example.repositories.implementations.json.VehicleJsonRepository;
import org.example.services.Authentication;
import org.example.services.RentalService;
import org.example.services.VehicleService;

public class Main {
    public static void main(String[] args) {

        String storageType = "json";

        UserRepository userRepo;
        VehicleRepository vehicleRepo;
        RentalRepository rentalRepo;

        switch (storageType) {
            case "jdbc" -> {
                userRepo = new UserJdbcRepository();
                vehicleRepo = new VehicleJdbcRepository();
                rentalRepo = new RentalJdbcRepository();
            }
            case "json" -> {
                userRepo = new UserJsonRepository();
                vehicleRepo = new VehicleJsonRepository();
                rentalRepo = new RentalJsonRepository();
            }
            default -> throw new IllegalArgumentException("Unknown storage type: " + storageType);
        }
        Authentication authService = new Authentication(userRepo);
        VehicleService vehicleService = new VehicleService(vehicleRepo, rentalRepo);
        RentalService rentalService = new RentalService(rentalRepo);

        App app = new App(authService, vehicleService, rentalService);
        app.run();
    }
}