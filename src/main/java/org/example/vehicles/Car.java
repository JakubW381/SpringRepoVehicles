package org.example.vehicles;

public class Car extends Vehicle{
    public Car(String b, String m, int y, int p, Boolean r, int id) {
        super(b, m, y, p, r, id);
        classType = 1;
    }
}
