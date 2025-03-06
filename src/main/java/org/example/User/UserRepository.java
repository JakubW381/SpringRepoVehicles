package org.example.User;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class UserRepository implements IUserRepository {

    public List<User> userList = new ArrayList<>();
    private User currentUser;

    UserRepository(String login, String password){
        getUsers();
        Authentication auth = new Authentication();
        currentUser = auth.init(login,password,this);
    }

    @Override
    public User getUser() {
        return currentUser;
    }

    @Override
    public void getUsers() {
        try{
            List<User> newUser = new ArrayList<>();
            File file = new File("src/userList.txt");
            Scanner myReader = new Scanner(file);
            while (myReader.hasNextLine()) {
                String data = myReader.nextLine();
                String[] datas = data.split(";");
                if (datas.length == 4) {
                    try {
                        User user = new User(
                                datas[0],
                                datas[1],
                                Integer.parseInt(datas[2]),
                                Integer.parseInt(datas[3])
                        );
                        newUser.add(user);
                    } catch (NumberFormatException e) {
                        System.err.println("Błąd parsowania danych: " + data);
                    }
                }
            }
            myReader.close();
            userList.clear();
            for(User u : newUser){
                userList.add(u);
                //System.out.println(u.toCSV());
            }
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    @Override
    public void save() {

        try {
            File fold = new File("src/userList.txt");
            fold.delete();
            File file = new File("src/userList.txt");
            FileWriter myWriter = new FileWriter(file);
            for(User u : userList){
                myWriter.append(u.toCSV()+"\n");
            }
            myWriter.close();
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }
}
