package org.example.repositories.implementations;

import com.google.common.reflect.TypeToken;
import org.example.util.JsonFileStorage;
import org.example.models.Rental;
import org.example.repositories.RentalRepository;

import java.util.*;

public class RentalJsonRepository implements RentalRepository {

    private final List<Rental> rentalList;


    private final JsonFileStorage<Rental> storage =
            new JsonFileStorage<>("rentals.json", new TypeToken<List<Rental>>(){}.getType());

    public RentalJsonRepository() {
        this.rentalList = new ArrayList<>(storage.load());
    }

    @Override
    public List<Rental> findAll() {
        return new ArrayList<>(rentalList);
    }

    @Override
    public Optional<Rental> findById(String id) {
        return rentalList.stream().filter(r ->
                r.getId().equals(id)).findFirst();
    }
    @Override
    public Optional<List<Rental>> findByVehicleId(String id) {
        return Optional.of(rentalList.stream()
                .filter(r -> Objects.equals(r.getVehicleId(), id))
                .toList());
    }

    @Override
    public Optional<List<Rental>> findByUserId(String id) {

        return Optional.of(rentalList.stream()
                .filter(r -> Objects.equals(r.getUserId(), id))
                .toList());
    }

    @Override
    public Rental save(Rental rental) {
        if(rental.getId() == null || rental.getId().isBlank()){
            rental.setId(UUID.randomUUID().toString());
        }else{
            deleteById(rental.getId());
        }
        rentalList.add(rental);
        storage.save(rentalList);
        return rental;
    }

    @Override
    public void deleteById(String id) {
        rentalList.removeIf(v -> v.getId().equals(id));
        storage.save(rentalList);
    }
}
