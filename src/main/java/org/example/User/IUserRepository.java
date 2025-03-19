package org.example.User;


public interface IUserRepository {

    User getUser();
    void getUsers();

    void addUser(String username, String pass);

    void save();
}
