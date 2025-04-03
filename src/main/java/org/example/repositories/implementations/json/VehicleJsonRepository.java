package org.example.repositories.implementations.json;

import com.google.common.reflect.TypeToken;
import org.example.db.JsonFileStorage;
import org.example.models.User;
import org.example.models.Vehicle;
import org.example.repositories.VehicleRepository;

import java.util.*;

public class VehicleJsonRepository implements VehicleRepository {

    private final List<Vehicle> vehicles;

    private final JsonFileStorage<Vehicle> storage =
        new JsonFileStorage<>("vehicles.json", new TypeToken<List<Vehicle>>(){}.getType());

    public VehicleJsonRepository() {
        this.vehicles = new ArrayList<>(storage.load());
    }


    public List<Vehicle> getList(){
        return vehicles;
    }

    public void printVehicles(){

        for (Vehicle v :vehicles){
            System.out.println(v.printVehicle());
        }
    }

    @Override
    public void addVehicle(Vehicle veh, User user){
        if (user.getRole().equals("ADMIN")){
            save(veh);
        }else{
            System.out.println("No premission");
        }
    }

    @Override
    public List<Vehicle> findAll() {
        return new ArrayList<>(vehicles);
    }

    @Override
    public Optional<Vehicle> findById(String id) {
        return vehicles.stream().filter(v ->
                v.getId().equals(id)).findFirst();
    }


    @Override
    public Vehicle save(Vehicle vehicle) {
        if(vehicle.getId() == null || vehicle.getId().isBlank()){
            vehicle.setId(UUID.randomUUID().toString());
        }else{
            deleteById(vehicle.getId());
        }
        vehicles.add(vehicle);
        storage.save(vehicles);
        return vehicle;
    }

    @Override
    public void deleteById(String id) {
        vehicles.removeIf(v -> v.getId().equals(id));
        storage.save(vehicles);
    }
}