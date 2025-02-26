package org.example.vehicles;

public abstract class Vehicle {

    public int classType;
    public String brand;
    public String model;
    public int year;
    public int price;
    public Boolean rented;
    public int ID;

    Vehicle(String b, String m,int y,int p,Boolean r,int id){
        brand = b;
        model = m;
        year = y;
        rented = r;
        price = p;
        ID =id;
    }

    public String toCSV(){
        return (classType+";"+ID+";"+brand+";"+model+";"+year+";"+price+";"+rented+";");
    }
}
