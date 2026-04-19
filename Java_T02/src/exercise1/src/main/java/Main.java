import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int numberLine = 0;
        while (true) {
            System.out.println("put number pets");
            try {
                numberLine = scanner.nextInt();
                scanner.nextLine();
                if (numberLine > 0) {
                    break;
                } else {
                    System.out.println("Number must be positive. Please try again.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Could not parse a number. Please, try again");
                scanner.nextLine();
            }
        }

        List<Animal> pets = new ArrayList<>();

        for (int i = 0; i < numberLine; i++) {
            System.out.println("Write pet type dog or cat");
            String petType = scanner.next().trim().toLowerCase();
            String namePets = scanner.next();
            int age = 0;
            if (!"dog".equals(petType) && !"cat".equals(petType)) {
                System.out.println("Incorrect input. Unsupported pet type");
                scanner.nextLine();
                continue;
            }
            try {
                age = scanner.nextInt();
                scanner.nextLine();
            } catch (InputMismatchException e) {
                System.out.println("Could not parse a number. Please, try again");
                scanner.nextLine();
                i--;
                continue;
            }
            if (age <= 0) {
                System.out.println("Incorrect input. Age <= 0");
                continue;
            }

            if("dog".equals(petType)) {
                pets.add(new Dog(namePets, age));
            }
            else {
                pets.add(new Cat(namePets, age));
            }

        }
        for (Animal pet : pets) {
            System.out.println(pet);
        }
        scanner.close();
    }
}

