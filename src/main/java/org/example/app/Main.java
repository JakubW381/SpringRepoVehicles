package org.example.app;

import org.example.config.HibernateConfig;
import org.example.repositories.hibernate.RentalHibernateRepository;
import org.example.repositories.hibernate.UserHibernateRepository;
import org.example.repositories.hibernate.VehicleHibernateRepository;

import org.example.services.hibernate.AuthHibernateService;
import org.example.services.hibernate.RentalHibernateService;
import org.example.services.hibernate.VehicleHibernateService;

public class Main {
    public static void main(String[] args) {

        UserHibernateRepository userRepo;
        VehicleHibernateRepository vehicleRepo;
        RentalHibernateRepository rentalRepo;

        userRepo = new UserHibernateRepository();
        vehicleRepo = new VehicleHibernateRepository();
        rentalRepo = new RentalHibernateRepository();

        AuthHibernateService authService = new AuthHibernateService(userRepo);
        VehicleHibernateService vehicleService = new VehicleHibernateService(rentalRepo,vehicleRepo);
        RentalHibernateService rentalService = new RentalHibernateService(rentalRepo,vehicleRepo,userRepo);

        AppHibernate app = new AppHibernate(authService, vehicleService, rentalService);
        app.run();

    }
}