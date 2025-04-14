package org.example.repositories.hibernate;


import org.example.models.Rental;
import org.example.repositories.RentalRepository;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.util.List;
import java.util.Optional;

public class RentalHibernateRepository implements RentalRepository {

    private Session session;

    public void setSession(Session session){
        this.session = session;
    }

    @Override
    public List<Rental> findAll() {
        return session.createQuery("FROM Rental", Rental.class).list();
    }

    @Override
    public Optional<Rental> findById(String id) {
        return Optional.ofNullable(session.get(Rental.class,id));
    }

    @Override
    public Optional<List<Rental>> findByVehicleId(String id) {
        List<Rental> rentals = session.createQuery("""
        From Rental r
        Where r.vehicle.id = :id
        """, Rental.class)
                .setParameter("id", id)
                .getResultList();

        return rentals.isEmpty() ? Optional.empty() : Optional.of(rentals);
    }

    @Override
    public Optional<List<Rental>> findByUserId(String id) {
        List<Rental> rentals = session.createQuery("""
        From Rental r
        Where r.users.id = :id
        """, Rental.class)
                .setParameter("id", id)
                .getResultList();

        return rentals.isEmpty() ? Optional.empty() : Optional.of(rentals);
    }

    @Override
    public Rental save(Rental rental) {
        return session.merge(rental);
    }

    @Override
    public void deleteById(String id) {
        Rental rental = session.get(Rental.class,id);
        if(rental != null){
            session.remove(rental);
        }
    }

    @Override
    public Optional<Rental> findByVehicleIdAndReturnDateIsNull(String vehicleId) {
        Query<Rental> query = session.createQuery("""
               From Rental r
               Where r.vehicle.id = :vehicleId
               And r.returnDate Is Null"""
                ,Rental.class);
        query.setParameter("vehicleId", vehicleId);
        return query.uniqueResultOptional();
    }
}
