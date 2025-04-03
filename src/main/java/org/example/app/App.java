package org.example.app;

import org.example.models.Rental;
import org.example.models.User;
import org.example.models.Vehicle;
import org.example.services.Authentication;
import org.example.services.RentalService;
import org.example.services.VehicleService;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class App {

    private final Authentication authService;
    private final VehicleService vehicleService;
    private final RentalService rentalService;
    private final Scanner scanner = new Scanner(System.in);

    public App(Authentication authService, VehicleService vehicleService, RentalService rentalService) {
        this.authService = authService;
        this.vehicleService = vehicleService;
        this.rentalService = rentalService;
    }

    public void run() {
        System.out.println("Login or Register?");
        Scanner scanner = new Scanner(System.in);

        String s = scanner.nextLine();

        if (s.toLowerCase().equals("login")){
            System.out.println("Login:");
            String login = scanner.nextLine();
            System.out.println("Password:");
            String pass = scanner.nextLine();
            if(authService.login(login,pass) == false){
                return;
            }
        } else if (s.toLowerCase().equals("register")) {
            System.out.println("Login:");
            String login = scanner.nextLine();
            System.out.println("Password:");
            String pass = scanner.nextLine();
            if(authService.register(login,pass) == false){
                return;
            }
        }

        System.out.println("Hello "+authService.getCurrentUser().getLogin());

        System.out.println("\nInstructions");
        System.out.println("[show] - to show all the vehicles\n" +
                "[rent <id == (second row value)>] - rent vehicle of given id\n" +
                "[return <id == (second row value)>] - return vehicle of given id\n"+
                "[show me] - shows information about you");

        if (authService.getCurrentUser().getRole().equals("ADMIN")){
            System.out.println("\nAdmin Instructions");
            System.out.println("[add  <brand> <model> <year> <plate> <price> <category>] - to add Vehicle");
            System.out.println("[remove <id>] - to remove Vehicle");
            System.out.println("[show users] - shows list of Users");
        }

        for(Vehicle v : vehicleService.findAll()){
            Optional<List<Rental>> list = rentalService.findByVehicleId(v.getId());
            boolean available = true;
            if (list.isPresent()){
                for(Rental r : list.get().reversed()){
                    if (r.getReturnDateTime() == null){
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
            if (authService.getCurrentUser().getRole().equals("ADMIN")){
                if (s.split(" ")[0].equals("add")){
                    Vehicle veh = Vehicle.builder()
                            .brand(s.split(" ")[1])
                            .model(s.split(" ")[2])
                            .year(Integer.parseInt(s.split(" ")[3]))
                            .plate(s.split(" ")[4])
                            .price(Double.parseDouble(s.split(" ")[5]))
                            .category(s.split(" ")[6])
                            .build();
                    System.out.println("To add Attribute <name:value> ; To end type 'finish' ");
                    while(true){
                        s = scanner.nextLine();
                        if (s.split(" ")[0].equals("finish")){
                            break;
                        }
                        veh.addAttribute(s.split(":")[0],s.split(":")[1]);
                    }
                    vehicleService.save(veh);
                    System.out.println("Car added");
                }else if(s.split(" ")[0].equals("remove")){
                    vehicleService.deleteById(s.split(" ")[1]);
                }else if(s.equals("show users")){

                    for(User u : authService.findAll()){
                        System.out.println(u.printUser());
                        List<Rental> list = rentalService.findByUserId(u.getId()).get();
                        for(Rental r : list){
                            if (r.getReturnDateTime() == null && u.getId().equals(r.getUserId())){
                                System.out.println("Rent Date Time:" + r.getRentDateTime());
                                System.out.println(vehicleService.findById(r.getVehicleId()).get().printVehicle());
                            }
                        }
                    }
                }
            }
            if (s.equals("show")){
                for(Vehicle v : vehicleService.findAll()){
                    Optional<Rental> available = rentalService.findByVehicleIdAndReturnDateIsNull(v.getId());
                    if (!available.isPresent()){
                        System.out.println(v.printVehicle());
                    }
                }
            }else if(s.split(" ")[0].equals("rent")){
                Optional<Rental> rentable = rentalService.findByVehicleIdAndReturnDateIsNull(s.split(" ")[1]);
                if (!rentable.isPresent()){
                    LocalDateTime now = LocalDateTime.now();
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                    Rental rental = Rental.builder()
                            .rentDateTime(now.format(formatter).toString())
                            .userId(authService.getCurrentUser().getId())
                            .vehicleId(s.split(" ")[1])
                            .build();
                    rentalService.save(rental);
                }else{
                    System.out.println("This Car is not available");
                }
            }else if(s.split(" ")[0].equals("return")){
                for(Rental r :rentalService.findAll()){
                    if (r.getVehicleId().equals(s.split(" ")[1]) && authService.getCurrentUser().getId().equals(r.getUserId()) && r.getReturnDateTime()== null){
                        LocalDateTime now = LocalDateTime.now();
                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                        r.setReturnDateTime(now.format(formatter));
                        rentalService.save(r);
                        System.out.println("Vehicle "+s.split(" ")[1] + " returned");
                        break;
                    }
                }
            }else if(s.equals("show me")){
                System.out.println("User info ;id;username;hashedpassword;role;");
                System.out.println(authService.getCurrentUser().printUser());
                System.out.println("Rented Vehicles:\n");
                List<Rental> list = rentalService.findByUserId(authService.getCurrentUser().getId()).get();

                for(Rental r : list){
                    if (r.getReturnDateTime()== null){
                        System.out.println("Rent Date Time:" + r.getRentDateTime());
                        System.out.println(vehicleService.findById(r.getVehicleId()).get().printVehicle());
                    }
                }
            }
        }
        scanner.close();
    }
}
