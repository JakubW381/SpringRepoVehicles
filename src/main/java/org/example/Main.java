package org.example;

import org.example.vehicles.*;
import java.util.Scanner;


public class Main {
    public static void main(String[] args) {
//        VehicleRepository.getVehicle();
//
//        VehicleRepository.rentVehicle(3);
//        VehicleRepository.returnVehicle(3);
            String s = "";
            while(s != "bye") {
                Scanner scanner = new Scanner(System.in);
                s = scanner.nextLine();

                if (s == "show"){
                    VehicleRepository.getVehicle();

                }else if(s.split(" ")[0] == "rent"){
                    VehicleRepository.rentVehicle(Integer.parseInt(s.split(" ")[1]));
                }else if(s.split(" ")[0] == "return"){
                    VehicleRepository.returnVehicle(Integer.parseInt(s.split(" ")[1]));
                }
                scanner.close();
            }





    }
}