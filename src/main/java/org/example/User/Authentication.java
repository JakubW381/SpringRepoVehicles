package org.example.User;

public class Authentication {

    public User init(String login, String password,UserRepository repo){
        for (User u : repo.userList){
            if (u.getLogin().equals(login)){
                if (u.getPassword().equals(password)){
                    return u;
                }else{
                    System.out.println("Wrong password");
                    return null;
                }
            }
        }
        System.out.println("Wrong credentials");
        return null;
    }



}
