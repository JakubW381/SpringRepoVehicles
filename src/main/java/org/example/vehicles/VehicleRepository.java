package org.example.vehicles;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class VehicleRepository implements IVehicleRepositotry{

    @Override
    public void rentVehicle(int id){
        for(Vehicle v : vehicles){
            if ( v.ID == id && v.rented == false){
                v.rented = true;
            }
        }
        save();
        getVehicle();
    }
    @Override
    public void returnVehicle(int id){
        for(Vehicle v : vehicles){
            if ( v.ID == id && v.rented == true){
                v.rented = false;
            }
        }
        save();
        getVehicle();
    }
    @Override
    public void getVehicle(){
        try{
            List<Vehicle> newVeh = new ArrayList<>();

            File file = new File("src/vehicles.txt");
            Scanner myReader = new Scanner(file);
            while(myReader.hasNextLine()){
                String data = myReader.nextLine();
                String[] datas = data.split(";");
                if (Integer.parseInt(datas[0]) == 1){
                    Car car = new Car(
                            datas[2],
                            datas[3],
                            Integer.parseInt(datas[4]),
                            Integer.parseInt(datas[5]),
                            Boolean.parseBoolean(datas[6]),
                            Integer.parseInt(datas[1])
                    );
                    newVeh.add(car);
                }else{
                    Motorcycle mt = new Motorcycle(
                            datas[2],
                            datas[3],
                            Integer.parseInt(datas[4]),
                            Integer.parseInt(datas[5]),
                            Boolean.parseBoolean(datas[6]),
                            datas[7],
                            Integer.parseInt(datas[1])
                    );
                    newVeh.add(mt);
                }
            }
            myReader.close();
            vehicles.clear();
            for(Vehicle v : newVeh){
                vehicles.add(v);
                System.out.println(v.toCSV());
            }
            System.out.println();
        }catch (IOException e){
            e.printStackTrace();
        }
    }
    @Override
    public void save(){
        try {
            File fold = new File("src/vehicles.txt");
            fold.delete();
            File file = new File("src/vehicles.txt");
            FileWriter myWriter = new FileWriter(file);
            for(Vehicle v : vehicles){
                myWriter.append(v.toCSV()+"\n");
            }
            myWriter.close();
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

}
