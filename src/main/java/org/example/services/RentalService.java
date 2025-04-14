package org.example.services;

import org.example.models.Rental;

import java.util.List;
import java.util.Optional;

public interface RentalService {
    boolean isVehicleRented(String vehicleId);
    Rental rent(String vehicleId,String userId);
    Rental returnRental(String vehicleId, String userId);
    Optional<List<Rental>> findByUserId(String id);
    List<Rental> findAll();
}
