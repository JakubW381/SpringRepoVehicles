package org.example.User;

import com.google.common.hash.Hashing;

import java.nio.charset.StandardCharsets;

public class Authentication {

    public User init(String login, String password,UserRepository repo){
        String hashedPassword = Hashing.sha256()
                .hashString(password, StandardCharsets.UTF_8)
                .toString();
        for (User u : repo.userList){
            if (u.getLogin().equals(login)){
                if (u.getPassword().equals(hashedPassword)){
                    return u;
                }else{
                    System.out.println("Wrong password");
                    return null;
                }
            }
        }
        repo.addUser(login,password);
        System.out.println("Wrong credentials");
        return null;
    }



}
