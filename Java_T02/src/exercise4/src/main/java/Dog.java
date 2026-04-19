public class Dog extends Animal{
    public Dog(String name, int age) {
        super(name, age);
    }

    public String toString() {
        return "Dog name = " + getName()
                + ", age = " + getAge();
    }
}
