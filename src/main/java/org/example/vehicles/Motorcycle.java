package org.example.vehicles;

public class Motorcycle extends Vehicle{
    public String category;


    public Motorcycle(String b, String m, int y,int p, Boolean r,String cat, String id) {
        super(b, m, y,p, r, id);
        classType = 2;
        category = cat;
    }
    @Override
    public String toCSV(){
        return (classType+";"+ID+";"+brand+";"+model+";"+year+";"+price+";"+rented+";"+category+";");
    }

    public boolean equals(final Object object){
        if (super.equals(object)){
            Motorcycle that = (Motorcycle) object;
            if(this.category != that.category)return false;
            return true;
        }
        return false;
    }
}
