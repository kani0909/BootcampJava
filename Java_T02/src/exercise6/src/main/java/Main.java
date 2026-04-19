import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int numberLine = getNumberLine(scanner);
        System.out.println(numberLine);
        List<Animal> pets = new ArrayList<>();
        for (int i = 0; i < numberLine; i++) {
            System.out.println("Write pets type");
            String typePets = scanner.next().trim().toLowerCase();
            scanner.nextLine();
            if(!"cat".equals(typePets) && !"dog".equals(typePets)) {
                System.out.println("Incorrect input. Unsupported pet type");
                continue;
            }
            String namePets = scanner.next().trim();
            scanner.nextLine();
            int agePets;
            try {
                agePets = scanner.nextInt();
                scanner.nextLine();
            } catch (InputMismatchException e) {
                System.out.println("Could not parse a number. Please, try again");
                scanner.nextLine();
                continue;
            }

            if(agePets <= 0) {
                System.out.println("Incorrect input. Age <= 0");
                continue;
            }
            if ("dog".equals(typePets)) {
                pets.add(new Dog(namePets, agePets));
            } else {
                pets.add(new Cat(namePets, agePets));
            }
        }
        AnimalIterator iterator = new AnimalIterator(pets);
        while (iterator.hasNext()) {
            Animal animal = iterator.next();
            System.out.println(animal.toString());
        }
        scanner.close();
    }

    public static int getNumberLine(Scanner scanner){
        System.out.println("Put pets number");
        while (true) {
            try {
                int num = scanner.nextInt();
                scanner.nextLine();
                if (num > 0) {
                    return num;
                } else {
                    System.out.println("Could not parse a number. Please, try again");
                }
            } catch (InputMismatchException e) {
                System.out.println("Could not parse a number. Please, try again");
                scanner.nextLine();
            }
        }
    }
}
