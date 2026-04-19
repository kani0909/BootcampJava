import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

public class Main {
    public static void main(String[] args) {
        Animal.setProgramStartTime();
        Scanner scanner = new Scanner(System.in);
        int numberLine = getValidNumber(scanner);

        List<Animal> pets = new ArrayList<>();
        for (int i = 0; i < numberLine; i++) {
            String petsType = getAnimalType(scanner);
            if (petsType == null) {
                continue;
            }
            String namePets = scanner.next();
            int agePets = getPetsAge(scanner);
            if (agePets <= 0) {
                continue;
            }
            if ("cat".equals(petsType)) {
                pets.add(new Cat(namePets, agePets));
            } else {
                pets.add(new Dog(namePets, agePets));
            }

        }
        executeWalks(pets);

    }

    public static void executeWalks(List<Animal> pets) {
        if(pets.isEmpty()) {
            return;
        }
        ExecutorService executorService = Executors.newFixedThreadPool(pets.size());
        CountDownLatch latch = new CountDownLatch(pets.size());

        for (int i = 0; i < pets.size(); i++) {
            final Animal pet = pets.get(i);
            final int delay = i * 100;
            executorService.submit(() -> {
                try {
                    TimeUnit.MILLISECONDS.sleep(delay);
                    pet.goToWalk();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                } finally {
                    latch.countDown();
                }
            });
        }

        try {
            latch.await();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        executorService.shutdown();

        System.out.println("\nWalks result");
        for (Animal pet : pets) {
            if (pet instanceof Cat) {
                System.out.println(((Cat) pet).toWalkString());
            } else if (pet instanceof Dog) {
                System.out.println(((Dog) pet).toWalkString());
            }
        }

    }

    public static int getValidNumber(Scanner scanner) {
        while (true) {
            System.out.print("Enter the number of pets: ");
            try {
                int num = scanner.nextInt();
                scanner.nextLine();
                if (num > 0) {
                    return num;
                } else {
                    System.out.println("Number must be positive. Please try again.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Could not parse a number. Please try again.");
                scanner.nextLine();
            }
        }
    }

    public static String getAnimalType(Scanner scanner) {
        System.out.println("Write pet type");
        String petsType = scanner.next().trim().toLowerCase();

        boolean isValidType = Stream.of("dog", "cat")
                .anyMatch(type -> type.equals(petsType));

        if (isValidType) {
            return petsType;
        } else {
            System.out.println("Incorrect input. Unsupported pet type.");
            return null;
        }
    }

    public static int getPetsAge(Scanner scanner) {
        while (true) {
            try {
                int age = scanner.nextInt();
                scanner.nextLine();
                if (age > 0) {
                    return age;
                } else {
                    System.out.println("Incorrect input. Age <= 0");
                    return age;
                }

            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a valid number.");
                scanner.nextLine();
            }
        }
    }
}
