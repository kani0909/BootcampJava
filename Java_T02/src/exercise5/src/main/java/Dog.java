import java.time.Instant;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class Dog extends Animal{
    public Dog(String name, int age) {
        super(name, age);
    }

    public String toString() {
        return "Dog name = " + getName()
                +", age = " + getAge();
    }


    @Override
    public double goToWalk() throws InterruptedException {
        double walkDurationSeconds = getAge() * 0.5;
        long durationMillis = Math.round(walkDurationSeconds * 1000L);
        setStartWalkTime(Instant.now());
        TimeUnit.MILLISECONDS.sleep(durationMillis);
        setEndWalkTime(Instant.now());

        return walkDurationSeconds;
    }

    public String toWalkString() {
        return String.format("Dog name = %s, age = %d, start time = %.2f, end time = %.2f",
                getName(), getAge(),
                getSecondsFromStart(getStartWalkTime()),
                getSecondsFromStart(getEndWalkTime()));
    }

}
