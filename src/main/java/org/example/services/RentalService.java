package org.example.services;

import org.example.models.Rental;
import org.example.repositories.RentalRepository;

import java.util.List;
import java.util.Optional;

public class RentalService {

    RentalRepository repo;

    public RentalService(RentalRepository repo) {
        this.repo = repo;
    }

    public Optional<List<Rental>> findByVehicleId(String id) {
        return repo.findByVehicleId(id);
    }
    public List<Rental> findAll() {
        return repo.findAll();
    }
    public Optional<List<Rental>> findByUserId(String id) {
        return repo.findByUserId(id);
    }

    public Optional<Rental> findByVehicleIdAndReturnDateIsNull(String id) {
        return repo.findByVehicleIdAndReturnDateIsNull(id);
    }
    public void save(Rental rental) {
        repo.save(rental);
    }

}
