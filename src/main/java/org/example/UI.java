package org.example;

import org.example.vehicles.VehicleRepository;

import java.util.Scanner;

public class UI {

    private static VehicleRepository repo = new VehicleRepository();
    public static void init(){
        System.out.println("Instructions\n");
        System.out.println("[show] - to show all the vehicles\n" +
                "[rent <id == (second row value)>] - rent vehicle of given id\n" +
                "[return <id == (second row value)>] - return vehicle of given id");

        String s = "";
        Scanner scanner = new Scanner(System.in);
        while(s != "bye") {
            s = scanner.nextLine();

            if (s.equals("show")){
                repo.getVehicle();
            }else if(s.split(" ")[0].equals("rent")){
                repo.rentVehicle(Integer.parseInt(s.split(" ")[1]));
            }else if(s.split(" ")[0].equals("return")){
                repo.returnVehicle(Integer.parseInt(s.split(" ")[1]));
            }
        }
        scanner.close();
    }
}
