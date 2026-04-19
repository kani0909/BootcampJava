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

        String namePets = null;
        int agePets = 0;
        Double mass = 0.0;
        for (int i = 0; i < numberLine; i++) {
            System.out.println("Write pet type dog or cat");
            String petType = scanner.next().trim().toLowerCase();
            if (!"dog".equals(petType) && !"cat".equals(petType)) {
                System.out.println("Incorrect input. Unsupported pet type");
                scanner.nextLine();
                continue;
            }
            System.out.println("Write name");
            namePets = scanner.next();
            scanner.nextLine();
            System.out.println("age");
            agePets = scanner.nextInt();
            if (agePets <= 0) {
                System.out.println("Incorrect input. Age <= 0");
                continue;
            }
            scanner.nextLine();
            System.out.println("mass");
            mass = scanner.nextDouble();
            if (mass <= 0) {
                System.out.println("Incorrect input. Mass <= 0");
                continue;
            }
            scanner.nextLine();
            if ("dog".equals(petType)) {
                pets.add(new Dog(namePets, agePets, mass));
            }
            else {
                pets.add(new Cat(namePets, agePets, mass));
            }
        }

        for (Animal pet : pets) {
            System.out.println(pet);
        }

    }
}
