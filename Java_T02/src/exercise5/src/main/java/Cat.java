import java.time.Instant;
import java.util.concurrent.TimeUnit;

public class Cat extends Animal{
    public Cat(String name, int age) {
        super(name, age);
    }


    @Override
    public double goToWalk() throws InterruptedException {
        double walkDurationSeconds = getAge() * 0.25;
        long durationMillis = Math.round(walkDurationSeconds * 1000L);
        setStartWalkTime(Instant.now());
        TimeUnit.MILLISECONDS.sleep(durationMillis);
        setEndWalkTime(Instant.now());
        return walkDurationSeconds;
    }


    public  String toString() {
        return "Cat name = " + getName()
                + ", age = " + getAge();
    }

    public String toWalkString() {
        return String.format("Cat name = %s, age = %d, start time = %.2f, end time = %.2f",
                getName(), getAge(),
                getSecondsFromStart(getStartWalkTime()),
                getSecondsFromStart(getEndWalkTime()));
    }

}
