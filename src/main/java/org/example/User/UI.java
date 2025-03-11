package org.example.User;

import org.example.vehicles.Car;
import org.example.vehicles.Motorcycle;
import org.example.vehicles.Vehicle;
import org.example.vehicles.VehicleRepository;

import java.util.Scanner;
import java.util.UUID;

public class UI {

    private static VehicleRepository repo = new VehicleRepository();
    public static void init(){

        Scanner scanner = new Scanner(System.in);
        System.out.println("Login");
        String login = scanner.nextLine();
        System.out.println("Password");
        String password = scanner.nextLine();


        UserRepository userRepo = new UserRepository(login,password);
        if (userRepo.getUser() == null) {
            return;
        }

        System.out.println("Hello "+userRepo.getUser().getLogin());

        System.out.println("\nInstructions");
        System.out.println("[show] - to show all the vehicles\n" +
                "[rent <id == (second row value)>] - rent vehicle of given id\n" +
                "[return <id == (second row value)>] - return vehicle of given id\n"+
                "[show me] - shows information about you");

        if (userRepo.getUser().getRole() == 0){
            System.out.println("\nAdmin Instructions");
            System.out.println("[add] - to add Vehicle");
            System.out.println("for Car [add Car <brand> <model> <year> <price>]");
            System.out.println("for Motorcycle [add Motorcycle <brand> <model> <year> <price> <category>]");
            System.out.println("[remove <id>] - to remove Vehicle");
            System.out.println("[show users] - shows list of users");
        }

        repo.getVehicle();
        String s = "";
        while(!s.equals("bye")) {
            s = scanner.nextLine();
            if (userRepo.getUser().getRole() == 0){
                if (s.split(" ")[0].equals("add")){
                    if (s.split(" ")[1].equals("Car")){
                        Car nCar = new Car(
                                s.split(" ")[2],
                                s.split(" ")[3],
                                Integer.parseInt(s.split(" ")[4]),
                                Integer.parseInt(s.split(" ")[5]),
                                false,
                                UUID.randomUUID().toString()
                        );
                        repo.addVehicle(nCar,userRepo.getUser());
                    }else if (s.split(" ")[1].equals("Motorcycle")){
                        Motorcycle nMot = new Motorcycle(
                                s.split(" ")[2],
                                s.split(" ")[3],
                                Integer.parseInt(s.split(" ")[4]),
                                Integer.parseInt(s.split(" ")[5]),
                                false,
                                s.split(" ")[6],
                                UUID.randomUUID().toString()
                        );
                        repo.addVehicle(nMot,userRepo.getUser());
                    }else{
                        System.out.println("Wrong Vehicle type");
                    }
                }else if(s.split(" ")[0].equals("remove")){
                    repo.removeVehicle(s.split(" ")[1],userRepo);
                }else if(s.equals("show users")){
                    userRepo.getUsers();
                    repo.getVehicle();
                    for (User u :userRepo.userList){
                        if (u.getRentedHash() != -1){
                            for (Vehicle v : repo.getList()){
                                if (v.hashCode() == u.getRentedHash()){
                                    System.out.println(u.toCSV() +" renting --> "+ v.toCSV());
                                    break;
                                }
                            }
                        }else{
                            System.out.println(u.toCSV());
                        }
                    }
                }
            }
            if (s.equals("show")){
                repo.getVehicle();
                for (Vehicle v : repo.getList()){
                    System.out.println(v.toCSV());
                }
            }else if(s.split(" ")[0].equals("rent")){
                repo.rentVehicle(s.split(" ")[1],userRepo);
            }else if(s.split(" ")[0].equals("return")){
                repo.returnVehicle(s.split(" ")[1],userRepo);
            }else if(s.equals("show me")){
                for (User u :userRepo.userList){
                    if (u.equals(userRepo.getUser())){
                        if (u.getRentedHash() != -1){
                            for (Vehicle v : repo.getList()){
                                if (v.hashCode() == u.getRentedHash()){
                                    System.out.println(u.toCSV() +" renting --> "+ v.toCSV());
                                    break;
                                }
                            }
                        }else{
                            System.out.println(u.toCSV());
                        }
                    }
                }
            }
        }
        scanner.close();
    }
}
