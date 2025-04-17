package org.example.services.hibernate;

import org.example.config.HibernateConfig;
import org.example.models.Rental;
import org.example.models.User;
import org.example.models.Vehicle;
import org.example.repositories.hibernate.RentalHibernateRepository;
import org.example.repositories.hibernate.UserHibernateRepository;
import org.example.repositories.hibernate.VehicleHibernateRepository;
import org.example.services.RentalService;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class RentalHibernateService implements RentalService {

    private final RentalHibernateRepository rentalHibernateRepository;
    private final VehicleHibernateRepository vehicleHibernateRepository;
    private final UserHibernateRepository userHibernateRepository;

    public RentalHibernateService(RentalHibernateRepository rentalHibernateRepository, VehicleHibernateRepository vehicleHibernateRepository, UserHibernateRepository userHibernateRepository) {
        this.rentalHibernateRepository = rentalHibernateRepository;
        this.vehicleHibernateRepository = vehicleHibernateRepository;
        this.userHibernateRepository = userHibernateRepository;
    }

    @Override
    public boolean isVehicleRented(String vehicleId) {
        try(Session session = HibernateConfig.getSessionFactory().openSession()){
            rentalHibernateRepository.setSession(session);
            return rentalHibernateRepository.findByVehicleIdAndReturnDateIsNull(vehicleId).isPresent();
        }
    }

    @Override
    public Rental rent(String vehicleId, String userId) {
        Transaction tx = null;

        try(Session session = HibernateConfig.getSessionFactory().openSession()){
            tx = session.beginTransaction();

            rentalHibernateRepository.setSession(session);
            userHibernateRepository.setSession(session);
            vehicleHibernateRepository.setSession(session);

            if(rentalHibernateRepository.findByVehicleIdAndReturnDateIsNull(vehicleId).isPresent()){
                throw new IllegalStateException("Vehicle is rented");
            }

            Vehicle vehicle = vehicleHibernateRepository.findById(vehicleId)
                    .orElseThrow(() -> new RuntimeException("Vehicle not found"));
            User user = userHibernateRepository.findById(userId)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            Rental rental = Rental.builder()
                    .id(UUID.randomUUID().toString())
                    .vehicle(vehicle)
                    .user(user)
                    .rentDateTime(LocalDateTime.now().toString())
                    .build();

            rentalHibernateRepository.save(rental);
            tx.commit();
            return rental;
        }catch (Exception e){
            if (tx != null) {
                try {
                    if (tx.isActive()) tx.rollback();
                } catch (Exception ex) {
                    System.err.println("Rollback failed: " + ex.getMessage());
                }
            }
            throw e;
        }
    }

    @Override
    public Rental returnRental(String vehicleId, String userId) {
        Transaction tx = null;

        try(Session session = HibernateConfig.getSessionFactory().openSession()){
            tx = session.beginTransaction();

            rentalHibernateRepository.setSession(session);
            userHibernateRepository.setSession(session);
            vehicleHibernateRepository.setSession(session);

            Rental rental = rentalHibernateRepository.findByVehicleIdAndReturnDateIsNull(vehicleId).get();
            if(rental != null && userId.equals(rental.getUser().getId())){

            Vehicle vehicle = vehicleHibernateRepository.findById(vehicleId)
                    .orElseThrow(() -> new RuntimeException("Vehicle not found"));
            User user = userHibernateRepository.findById(userId)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            rental.setReturnDateTime(LocalDateTime.now().toString());
            rentalHibernateRepository.save(rental);
            tx.commit();
            return rental;
        }
        }catch (Exception e){
            if (tx != null) {
                try {
                    if (tx.isActive()) tx.rollback();
                } catch (Exception ex) {
                    System.err.println("Rollback failed: " + ex.getMessage());
                }
            }
            throw e;
        }
        return null;
    }

    @Override
    public Optional<List<Rental>> findByUserId(String id) {
        Transaction tx = null;
        try(Session session = HibernateConfig.getSessionFactory().openSession()){
            tx = session.beginTransaction();
            rentalHibernateRepository.setSession(session);
            tx.commit();
            return rentalHibernateRepository.findByUserId(id);
        }catch (Exception e){
            if (tx != null) {
                try {
                    if (tx.isActive()) tx.rollback();
                } catch (Exception ex) {
                    System.err.println("Rollback failed: " + ex.getMessage());
                }
            }
            throw e;
        }
    }

    @Override
    public List<Rental> findAll() {
        Transaction tx = null;
        try(Session session = HibernateConfig.getSessionFactory().openSession()){
            tx = session.beginTransaction();
            rentalHibernateRepository.setSession(session);
            tx.commit();
            return rentalHibernateRepository.findAll();
        }catch (Exception e){
            if (tx != null) {
                try {
                    if (tx.isActive()) tx.rollback();
                } catch (Exception ex) {
                    System.err.println("Rollback failed: " + ex.getMessage());
                }
            }
            throw e;
        }
    }
}
