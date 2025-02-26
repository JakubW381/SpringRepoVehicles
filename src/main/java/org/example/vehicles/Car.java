package org.example.vehicles;

public class Car extends Vehicle{
    Car(String b, String m, int y,int p, Boolean r, int id) {
        super(b, m, y, p, r, id);
        classType = 1;
    }
}
