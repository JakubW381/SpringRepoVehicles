package org.example.vehicles;

import java.util.Objects;

public abstract class Vehicle {

    public int classType;
    public String brand;
    public String model;
    public int year;
    public int price;
    public Boolean rented;
    public int ID;

    public Vehicle(String b, String m,int y,int p,Boolean r,int id){
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

    @Override
    public int hashCode(){
        return Objects.hash(ID);
    }

    @Override
    public boolean equals(final Object object){
        if (object == this) return true;
        if (object == null || getClass() != object.getClass()) return false;
        Vehicle that = (Vehicle) object;
        if (that.brand != this.brand) return false;
        if (that.model != this.model) return false;
        if (that.year != this.year) return false;
        if (that.price != this.price) return false;
        if (that.rented != this.rented) return false;
        if (that.ID != this.ID) return false;
        return true;
    }
}
