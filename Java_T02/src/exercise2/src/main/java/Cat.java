public class Cat extends Animal{
    public Cat(String name, int age, Double mass) {
        super(name, age, mass);
    }

    @Override
    public double getFeedInfoKg() {
        return getMass() * 0.1;
    }

    @Override
    public String toString() {
        return "Cat name = " + getName() + ", Cat age = " + getAge()
                + ", mass = " + getMass() + ", feed = " + getFeedInfoKg();
    }
}
