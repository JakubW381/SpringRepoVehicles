package org.example.repositories.implementations.json;

import com.google.common.reflect.TypeToken;
import org.example.db.JsonFileStorage;
import org.example.models.User;
import org.example.repositories.UserRepository;

import java.util.*;

public class UserJsonRepository implements UserRepository {

    private final List<User> userList;

    private final JsonFileStorage<User> storage =
            new JsonFileStorage<>("users.json",new
                    TypeToken<List<User>>(){}.getType());


    public UserJsonRepository(){
        this.userList = new ArrayList<>(storage.load());
    }

    public void printUsers(){
        for (User u :userList){
            System.out.println(u.printUser());
        }
    }

    @Override
    public List<User> findAll() {
        return new ArrayList<>(userList);
    }

    @Override
    public Optional<User> findById(String id) {
        return userList.stream().filter( u ->
                u.getId().equals(id)).findFirst();
    }

    @Override
    public Optional<User> findByLogin(String login) {
        return userList.stream().filter( u ->
                u.getLogin().equals(login)).findFirst();
    }

    @Override
    public User save(User user) {
        if(user.getId() == null || user.getId().isBlank()){
            user.setId(UUID.randomUUID().toString());
        }else{
            deleteById(user.getId());
        }
        userList.add(user);
        storage.save(userList);
        return user;
    }

    @Override
    public void deleteById(String id) {
        userList.removeIf(u -> u.getId().equals(id));
        storage.save(userList);
    }
}
