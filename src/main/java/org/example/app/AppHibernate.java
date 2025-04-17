package org.example.app;

import org.example.models.Rental;
import org.example.models.User;
import org.example.models.Vehicle;
import org.example.services.AuthService;
import org.example.services.RentalService;
import org.example.services.VehicleService;

import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class AppHibernate {

    private final AuthService authService;
    private final VehicleService vehicleService;
    private final RentalService rentalService;
    private final Scanner scanner = new Scanner(System.in);

    public AppHibernate(AuthService authService, VehicleService vehicleService, RentalService rentalService) {
        this.authService = authService;
        this.vehicleService = vehicleService;
        this.rentalService = rentalService;
    }

    public void run(){
        User currentUser = null;
        System.out.println("Login or Register?");
        String s = scanner.nextLine();

        try{
            if (s.toLowerCase().equals("login")){
                System.out.println("Login:");
                String login = scanner.nextLine();
                System.out.println("Password:");
                String pass = scanner.nextLine();
                Optional<User> loginResult = authService.login(login, pass);
                if (loginResult.isPresent()) {
                    currentUser = loginResult.get();
                } else {
                    System.out.println("Błędny login lub hasło.");
                }

            } else if (s.toLowerCase().equals("register")) {
                System.out.println("Login:");
                String login = scanner.nextLine();
                System.out.println("Password:");
                String pass = scanner.nextLine();
                currentUser = authService.register(login,pass,"USER");
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        if (currentUser != null) {
            System.out.println("Hello " + currentUser.getLogin());
        } else {
            System.out.println("Nie udało się zalogować ani zarejestrować użytkownika.");
            return;
        }


        System.out.println("\nInstructions");
        System.out.println("[show] - to show all the vehicles\n" +
                "[rent <id == (second row value)>] - rent vehicle of given id\n" +
                "[return <id == (second row value)>] - return vehicle of given id\n"+
                "[show me] - shows information about you");

        if (currentUser.getRole().equals("ADMIN")){
            System.out.println("\nAdmin Instructions");
            System.out.println("[add  <brand> <model> <year> <plate> <price> <category>] - to add Vehicle");
            System.out.println("[remove <id>] - to remove Vehicle");
            System.out.println("[show users] - shows list of Users");
        }


        for(Vehicle v : vehicleService.findAvailableVehicles()){
            System.out.println(v.printVehicle());
        }
        s = "";

        while(!s.equals("bye")) {
            s = scanner.nextLine();
            if (currentUser.getRole().equals("ADMIN")){
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
                        List<Rental> list = rentalService.findAll().stream().filter(rental -> rental.getReturnDateTime() == null).toList();
                        for(Rental r : list){
                            if (u.getId().equals(r.getUser().getId())){
                                System.out.println("Rent Date Time:" + r.getRentDateTime());
                                System.out.println(r.getVehicle().toString());
                            }
                        }
                    }
                }
            }
            if (s.equals("show")){
                for(Vehicle v : vehicleService.findAvailableVehicles()){
                    System.out.println(v.printVehicle());
                }
            }
            else if(s.split(" ")[0].equals("rent")){
                rentalService.rent(s.split(" ")[1],currentUser.getId());
            }
            else if(s.split(" ")[0].equals("return")){
                rentalService.returnRental(s.split(" ")[1], currentUser.getId());
            }else if(s.equals("show me")){
                System.out.println("User info ;id;username;hashedpassword;role;");
                System.out.println(currentUser.printUser());
                System.out.println("Rented Vehicles:\n");
                List<Rental> list = rentalService.findByUserId(currentUser.getId()).get();
                for(Rental r : list){
                    if (r.getReturnDateTime() == null){
                        System.out.println("Rent Date Time:" + r.getRentDateTime());
                        System.out.println(r.getVehicle().toString());
                    }
                }
            }
        }
        scanner.close();
    }
}
