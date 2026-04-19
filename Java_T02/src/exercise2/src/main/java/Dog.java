public class Dog extends Animal{
    public Dog(String name, int age, Double mass) {
        super(name, age, mass);
    }

    @Override
    public double getFeedInfoKg() {
        return this.getMass() * 0.3;
    }

    @Override
    public String toString() {
        return "Dog name = " + getName() + ", Dog age = " + getAge() +
                 ", mass = " + getMass() + ", feed = " + getFeedInfoKg();
    }
}
