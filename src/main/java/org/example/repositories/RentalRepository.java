package org.example.repositories;

import org.example.models.Rental;

import java.util.List;
import java.util.Optional;

public interface RentalRepository {
    List<Rental> findAll();
    Optional<Rental> findById(String id);

    Optional<List<Rental>> findByVehicleId(String id);

    Optional<List<Rental>> findByUserId(String id);
    Rental save(Rental user);
    void deleteById(String id);
}
