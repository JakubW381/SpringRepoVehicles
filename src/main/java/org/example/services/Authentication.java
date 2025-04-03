package org.example.services;

import at.favre.lib.crypto.bcrypt.BCrypt;
import org.example.models.User;
import org.example.repositories.UserRepository;
import org.example.repositories.implementations.json.UserJsonRepository;

import java.util.List;
import java.util.Optional;

public class Authentication {

    UserRepository userRepo;

    public Authentication(UserRepository userRepo) {
        this.userRepo = userRepo;
    }
    private User currentUser;

    public  User getCurrentUser() {
        return currentUser;
    }

    public  void setCurrentUser(User currentUser) {
        this.currentUser = currentUser;
    }



    public boolean login(String username, String password) {
        Optional<User> user = userRepo.findByLogin(username);

        if (user.isPresent()) {
            BCrypt.Result result = BCrypt.verifyer().verify(password.toCharArray(), user.get().getPassword());
            if (result.verified) {
                setCurrentUser(user.get());
                return true;
            }
        }

        System.out.println("Invalid username or password.");
        return false;
    }

    public boolean register(String username, String password) {
        if (userRepo.findByLogin(username).isEmpty()) {
            String hashedPassword = BCrypt.withDefaults().hashToString(12, password.toCharArray());
            User nUser = User.builder()
                    .login(username)
                    .password(hashedPassword)
                    .role("USER")
                    .build();
            userRepo.save(nUser);
            return login(username, password);
        }
        return false;
    }


    public List<User> findAll() {
        return userRepo.findAll();
    }
}
