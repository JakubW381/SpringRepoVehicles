package org.example.services.hibernate;

import org.example.config.HibernateConfig;
import org.example.models.Vehicle;
import org.example.repositories.hibernate.RentalHibernateRepository;
import org.example.repositories.hibernate.VehicleHibernateRepository;
import org.example.services.VehicleService;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;
import java.util.Optional;

public class VehicleHibernateService implements VehicleService {

    private final RentalHibernateRepository rentalHibernateRepository;
    private final VehicleHibernateRepository vehicleHibernateRepository;

    public VehicleHibernateService(RentalHibernateRepository rentalHibernateRepository, VehicleHibernateRepository vehicleHibernateRepository) {
        this.rentalHibernateRepository = rentalHibernateRepository;
        this.vehicleHibernateRepository = vehicleHibernateRepository;
    }

    @Override
    public List<Vehicle> findAll() {
        Transaction tx = null;
        try(Session session = HibernateConfig.getSessionFactory().openSession()){
            tx = session.beginTransaction();
            vehicleHibernateRepository.setSession(session);
            tx.commit();
            return vehicleHibernateRepository.findAll();
        }catch (Exception e){
            if( tx != null && tx.isActive()){
                tx.rollback();
            }throw e;
        }
    }


    @Override
    public Optional<Vehicle> findById(String id) {
        Transaction tx = null;
        try(Session session = HibernateConfig.getSessionFactory().openSession()){
            tx = session.beginTransaction();
            vehicleHibernateRepository.setSession(session);
            tx.commit();
            return vehicleHibernateRepository.findById(id);
        }catch (Exception e){
            if( tx != null && tx.isActive()){
                tx.rollback();
            }throw e;
        }
    }

    @Override
    public Vehicle save(Vehicle vehicle) {
        Transaction tx = null;
        try(Session session = HibernateConfig.getSessionFactory().openSession()){
            tx = session.beginTransaction();
            vehicleHibernateRepository.setSession(session);

            Vehicle vehicle1 =vehicleHibernateRepository.save(vehicle);

            tx.commit();
            return vehicle1;
        }catch (Exception e){
            if( tx != null && tx.isActive()){
                tx.rollback();
            }throw e;
        }
    }

    @Override
    public List<Vehicle> findAvailableVehicles() {
        Transaction tx = null;
        try(Session session = HibernateConfig.getSessionFactory().openSession()){
            tx = session.beginTransaction();
            vehicleHibernateRepository.setSession(session);

            List<Vehicle> list = vehicleHibernateRepository.findAll();
            for(Vehicle v : list){
                if(rentalHibernateRepository.findByVehicleIdAndReturnDateIsNull(v.getId()).isPresent()){
                    list.remove(v);
                }
            }
            tx.commit();
            return list;
        }catch (Exception e){
            if( tx != null && tx.isActive()){
                tx.rollback();
            }throw e;
        }
    }

    @Override
    public boolean isAvailable(String vehicleId) {
        return !rentalHibernateRepository.findByVehicleIdAndReturnDateIsNull(vehicleId).isPresent();
    }

    @Override
    public void deleteById(String id) {
        Transaction tx = null;
        try(Session session = HibernateConfig.getSessionFactory().openSession()){
            tx = session.beginTransaction();
            vehicleHibernateRepository.setSession(session);

            vehicleHibernateRepository.deleteById(id);

            tx.commit();
        }catch (Exception e){
            if( tx != null && tx.isActive()){
                tx.rollback();
            }throw e;
        }
    }
}
