package org.example.services;

import org.example.models.Vehicle;
import org.example.repositories.RentalRepository;
import org.example.repositories.VehicleRepository;

import java.util.List;
import java.util.Optional;

public class VehicleService {

    VehicleRepository vehRepo;
    RentalRepository renRepo;

    public VehicleService(VehicleRepository vehRepo, RentalRepository renRepo) {
        this.vehRepo = vehRepo;
        this.renRepo = renRepo;
    }

    public List<Vehicle> findAll() {
        return vehRepo.findAll();
    }
    public Optional<Vehicle> findById(String id) {
        return vehRepo.findById(id);
    }

    public void save(Vehicle veh) {
        vehRepo.save(veh);
    }
    public void deleteById(String id) {
        vehRepo.deleteById(id);
    }
}
