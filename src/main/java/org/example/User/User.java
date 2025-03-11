package org.example.User;

public class User {

    private String login;
    private String password;
    private int role;
    private int rentedHash;

    public User(String login, String password, int role, int rented) {
        this.login = login;
        this.password = password;
        this.role = role;
        this.rentedHash = rented;
    }
    public void setRentedHash(int hash){
        rentedHash = hash;
    }
    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }

    public int getRole() {
        return role;
    }

    public long getRentedHash() {
        return rentedHash;
    }

    public String toCSV(){
        return (login+";"+password+";"+role+";"+rentedHash+";");
    }
}
