package org.example.services.hibernate;

import at.favre.lib.crypto.bcrypt.BCrypt;
import org.example.config.HibernateConfig;
import org.example.models.User;
import org.example.repositories.hibernate.UserHibernateRepository;
import org.example.services.AuthService;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class AuthHibernateService implements AuthService {

    private final UserHibernateRepository userHibernateRepository;

    public AuthHibernateService(UserHibernateRepository userHibernateRepository) {
        this.userHibernateRepository = userHibernateRepository;
    }

    @Override
    public User register(String login, String rawPassword, String role) {
        Transaction tx = null;

        try(Session session = HibernateConfig.getSessionFactory().openSession()){
            tx = session.beginTransaction();

            userHibernateRepository.setSession(session);


            if(userHibernateRepository.findByLogin(login).isPresent()){
                throw new IllegalStateException("User already exists");
            }

            User user = User.builder()
                    .id(UUID.randomUUID().toString())
                    .login(login)
                    .password(BCrypt.withDefaults().hashToString(12, rawPassword.toCharArray()))
                    .role(role)
                    .build();

            userHibernateRepository.save(user);
            tx.commit();
            return user;
        }catch (Exception e){
            if( tx != null && tx.isActive()){
                tx.rollback();
            }throw e;
        }
    }

    @Override
    public Optional<User> login(String login, String rawPassword) {
        Transaction tx = null;

        try(Session session = HibernateConfig.getSessionFactory().openSession()){
            tx = session.beginTransaction();
            userHibernateRepository.setSession(session);
            Optional<User> userOpt = userHibernateRepository.findByLogin(login);
            if (userOpt.isEmpty()) {
                return Optional.empty();
            }

            User user = userOpt.get();
            BCrypt.Result result = BCrypt.verifyer().verify(rawPassword.toCharArray(), user.getPassword());

            tx.commit();
            return result.verified ? Optional.of(user) : Optional.empty();

        }catch (Exception e){
            if( tx != null && tx.isActive()){
                tx.rollback();
            }throw e;
        }
    }

    @Override
    public List<User> findAll() {
        Transaction tx = null;
        try(Session session = HibernateConfig.getSessionFactory().openSession()){
            tx = session.beginTransaction();
            userHibernateRepository.setSession(session);
            tx.commit();
            return userHibernateRepository.findAll();
        }catch (Exception e){
            if( tx != null && tx.isActive()){
                tx.rollback();
            }throw e;
        }
    }
}
