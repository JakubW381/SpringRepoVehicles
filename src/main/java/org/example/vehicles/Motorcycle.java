package org.example.vehicles;

public class Motorcycle extends Vehicle{
    public String category;


    Motorcycle(String b, String m, int y,int p, Boolean r,String cat, int id) {
        super(b, m, y,p, r, id);
        classType = 2;
        category = cat;
    }
    @Override
    public String toCSV(){
        return (classType+";"+ID+";"+brand+";"+model+";"+year+";"+price+";"+rented+";"+category+";");
    }
}
