import java.time.Instant;

abstract class Animal {
    private  String name;
    private int age;
    private Instant startWalkTime;
    private Instant endWalkTime;
    private static Instant programStartTime;

    public Animal(String name, int age) {
        this.name = name;
        this.age = age;
    }

    public abstract double goToWalk() throws InterruptedException;
    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public synchronized void setStartWalkTime(Instant instant) {
        this.startWalkTime = instant;
    }

    public synchronized void setEndWalkTime(Instant instant) {
        this.endWalkTime = instant;
    }

    public Instant getStartWalkTime() {
        return startWalkTime;
    }

    public Instant getEndWalkTime() {
        return endWalkTime;
    }

    public static Instant getProgramStartTime() {
        return programStartTime;
    }

    public static void setProgramStartTime() {
        programStartTime = Instant.now();
    }

    public double getSecondsFromStart(Instant time) {
        if (time == null) return 0;
        return (time.toEpochMilli() - programStartTime.toEpochMilli()) / 1000.0;
    }
}
