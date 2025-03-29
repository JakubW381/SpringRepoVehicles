package org.example.app;

import org.example.models.Rental;
import org.example.models.User;
import org.example.repositories.implementations.RentalJsonRepository;
import org.example.repositories.implementations.UserJsonRepository;
import org.example.models.Vehicle;
import org.example.repositories.implementations.VehicleJsonRepository;
import org.example.services.Authentication;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class UI {

    public static void init(){
        VehicleJsonRepository repo = new VehicleJsonRepository();
        UserJsonRepository userRepo = new UserJsonRepository();
        RentalJsonRepository rentalRepo = new RentalJsonRepository();
        Authentication auth = new Authentication();
        System.out.println("Login or Register?");
        Scanner scanner = new Scanner(System.in);

        String s = scanner.nextLine();

        if (s.toLowerCase().equals("login")){
            System.out.println("Login:");
            String login = scanner.nextLine();
            System.out.println("Password:");
            String pass = scanner.nextLine();
            if(auth.login(login,pass) == false){
                return;
            }
        } else if (s.toLowerCase().equals("register")) {
            System.out.println("Login:");
            String login = scanner.nextLine();
            System.out.println("Password:");
            String pass = scanner.nextLine();
            if(auth.register(login,pass) == false){
                return;
            }
        }

        System.out.println("Hello "+auth.getCurrentUser().getLogin());

        System.out.println("\nInstructions");
        System.out.println("[show] - to show all the vehicles\n" +
                            "[rent <id == (second row value)>] - rent vehicle of given id\n" +
                            "[return <id == (second row value)>] - return vehicle of given id\n"+
                            "[show me] - shows information about you");

        if (auth.getCurrentUser().getRole().equals("ADMIN")){
            System.out.println("\nAdmin Instructions");
            System.out.println("[add  <brand> <model> <year> <plate> <category>] - to add Vehicle");
            System.out.println("[remove <id>] - to remove Vehicle");
            System.out.println("[show users] - shows list of Users");
        }

        for(Vehicle v : repo.findAll()){
            Optional<List<Rental>> list = rentalRepo.findByVehicleId(v.getId());
            boolean available = true;
            if (list.isPresent()){
                for(Rental r : list.get().reversed()){
                    if (r.getReturnDateTime().equals("-")){
                        available = false;
                        break;
                    }
                }
            }
            if (available) System.out.println(v.printVehicle());
        }

        s = "";
        while(!s.equals("bye")) {
            s = scanner.nextLine();
            if (auth.getCurrentUser().getRole().equals("ADMIN")){
                if (s.split(" ")[0].equals("add")){
                    Vehicle veh = Vehicle.builder()
                            .brand(s.split(" ")[1])
                            .model(s.split(" ")[2])
                            .year(Integer.parseInt(s.split(" ")[3]))
                            .plate(s.split(" ")[4])
                            .category(s.split(" ")[5])
                            .build();
                    System.out.println("To add Attribute <name:value> ; To end type 'finish' ");
                    while(true){
                        s = scanner.nextLine();
                        if (s.split(" ")[0].equals("finish")){
                            break;
                        }
                        veh.addAttribute(s.split(":")[0],s.split(":")[1]);
                    }
                    repo.save(veh);
                    System.out.println("Car added");
                }else if(s.split(" ")[0].equals("remove")){
                    repo.deleteById(s.split(" ")[1]);
                }else if(s.equals("show users")){

                    for(User u : userRepo.findAll()){
                        System.out.println(u.printUser());
                        List<Rental> list = rentalRepo.findByUserId(u.getId()).get();
                        for(Rental r : list){
                            if (r.getReturnDateTime().equals("-")){
                                System.out.println("Rent Date Time:" + r.getRentDateTime());
                                System.out.println(repo.findById(r.getVehicleId()).get().printVehicle());
                            }
                        }
                    }
                }
            }
            if (s.equals("show")){
                for(Vehicle v : repo.findAll()){
                    Optional<List<Rental>> list = rentalRepo.findByVehicleId(v.getId());
                    boolean available = true;
                    if (list.isPresent()){
                        for(Rental r : list.get().reversed()){
                            if (r.getReturnDateTime().equals("-")){
                                available = false;
                                break;
                            }
                        }
                    }
                    if (available) System.out.println(v.printVehicle());
                }
            }else if(s.split(" ")[0].equals("rent")){
                boolean rentable = true;
                for(Rental r :rentalRepo.findAll()){
                    if (r.getVehicleId().equals(s.split(" ")[1]) && r.getReturnDateTime().equals("-")){
                        rentable = false;
                        break;
                    }
                }
                if (rentable){
                    LocalDateTime now = LocalDateTime.now();
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                    Rental rental = Rental.builder()
                            .rentDateTime(now.format(formatter).toString())
                            .returnDateTime("-")
                            .userId(auth.getCurrentUser().getId())
                            .vehicleId(s.split(" ")[1])
                            .build();
                    rentalRepo.save(rental);
                }else{
                    System.out.println("This Car is not available");
                }
            }else if(s.split(" ")[0].equals("return")){
                    for(Rental r :rentalRepo.findAll()){
                        if (r.getVehicleId().equals(s.split(" ")[1]) && r.getReturnDateTime().equals("-")){
                            LocalDateTime now = LocalDateTime.now();
                            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                            r.setReturnDateTime(now.format(formatter));
                            rentalRepo.save(r);
                            System.out.println("Vehicle "+s.split(" ")[1] + " returned");
                            break;
                        }
                    }
            }else if(s.equals("show me")){
                System.out.println("User info ;id;username;hashedpassword;role;");
                System.out.println(auth.getCurrentUser().printUser());
                System.out.println("Rented Vehicles:\n");
                List<Rental> list = rentalRepo.findByUserId(auth.getCurrentUser().getId()).get();

                for(Rental r : list){
                    if (r.getReturnDateTime().equals("-")){
                        System.out.println("Rent Date Time:" + r.getRentDateTime());
                        System.out.println(repo.findById(r.getVehicleId()).get().printVehicle());
                    }
                }
            }
        }
        scanner.close();
    }
}
