abstract class Animal {
    private String name;
    private int age;
    private Double mass;

    public Animal(String name, int age, Double mass) {
        this.name = name;
        this.age = age;
        this.mass = mass;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public Double getMass() {
        return mass;
    }

    public void setMass(Double mass) {
        this.mass = mass;
    }


    public abstract double getFeedInfoKg();
}
