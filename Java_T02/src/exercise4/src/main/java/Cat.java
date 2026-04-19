public class Cat extends Animal{
    public Cat(String name, int age) {
        super(name, age);
    }

    public String toString() {
        return "Cat name = " + getName()
                + ", age = " + getAge();
    }
}
