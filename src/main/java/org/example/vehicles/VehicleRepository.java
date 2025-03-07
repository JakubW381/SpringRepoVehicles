package org.example.vehicles;

import org.example.User.User;
import org.example.User.UserRepository;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class VehicleRepository implements IVehicleRepositotry{

    private List<Vehicle> vehicles = new ArrayList<>();

    public int getSize(){
        return vehicles.size();
    }
    public List<Vehicle> getList(){
        return vehicles;
    }

    @Override
    public void addVehicle(Vehicle veh, User user){
        if (user.getRole() == 0){
            for (Vehicle v : vehicles){
                if (v.ID == veh.ID) {
                    System.out.println("Wrong ID");
                    return;
                }
            }
            vehicles.add(veh);
            save();
            getVehicle();
        }else{
            System.out.println("No premission");
        }
    }
    @Override
    public void removeVehicle(int id, UserRepository userRepo){
        if (userRepo.getUser().getRole() == 0){
            boolean removed = false;
            for (Vehicle v : vehicles){
                if (v.ID == id) {
                    vehicles.remove(vehicles.indexOf(v));
                    for (User u : userRepo.userList){
                        if (u.getRentedHash() == v.hashCode()){
                            u.setRentedHash(-1);
                        }
                    }
                    removed = true;
                }
                if (removed) {
                    userRepo.save();
                    save();
                    getVehicle();
                    return;
                }
            }
        }else{
            System.out.println("No Premission");
        }
    }

    @Override
    public void rentVehicle(int id,UserRepository userRepo){
        if (userRepo.getUser().getRentedHash() == -1){
            for(Vehicle v : vehicles){
                if ( v.ID == id && !v.rented){
                    v.rented = true;
                    for (User u : userRepo.userList) {
                        if (u.equals(userRepo.getUser())){
                            u.setRentedHash(v.hashCode());
                            break;
                        }
                    }
                    userRepo.save();
                    save();
                    getVehicle();
                    return;
                }else{
                    System.out.println("Can't rent that vehicle");
                    break;
                }
            }
            save();
            getVehicle();
        }else{
            System.out.println("Cant rent more than one Vehicle");
        }
    }
    @Override
    public void returnVehicle(int id,UserRepository userRepo){
        if (userRepo.getUser().getRentedHash() != -1){
            var iterator = vehicles.iterator();
            while (iterator.hasNext()) {
                Vehicle v = iterator.next();
                if (v.ID == id && v.rented && v.hashCode() == userRepo.getUser().getRentedHash()) {
                    v.rented = false;

                    for (User u : userRepo.userList) {
                        if (u.equals(userRepo.getUser())) {
                            u.setRentedHash(-1);
                            break;
                        }
                    }
                    userRepo.save();
                    save();
                    getVehicle();
                    break;
                }
            }
        }else{
            System.out.println("Cant return that vehicle");
            getVehicle();
        }
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
                }else if (Integer.parseInt(datas[0]) == 2){
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
                //System.out.println(v.toCSV());
            }
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
