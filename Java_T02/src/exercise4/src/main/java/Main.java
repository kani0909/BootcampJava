import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Stream;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int numberLine = getValidNumber(scanner);


        List<Animal> pets = new ArrayList<>();

        createPets(numberLine, scanner, pets);
        pets.stream()
                .forEach(System.out::println);
        scanner.close();


    }

    private static void createPets(int numberLine, Scanner scanner, List<Animal> pets) {
        for (int i = 0; i < numberLine; i++) {
            String petsType = getPetsType(scanner);
            if (petsType == null) continue;
            String namePets = scanner.next();
            scanner.nextLine();
            Integer agePets = getAgePets(scanner);
            if (agePets == null) continue;

            if ("dog".equals(petsType)) {
                pets.add(new Dog(namePets, agePets));
            } else {
                pets.add(new Cat(namePets, agePets));
            }
        }
    }

    private static int getValidNumber(Scanner scanner) {
        int numberLine = Stream.generate(() -> {
            System.out.println("Put number pets");
            try {
                int num = scanner.nextInt();
                scanner.nextLine();
                if (num > 0) {
                    return num;
                } else {
                    System.out.println("Number must be positive. Please try again.");
                    return -1;
                }

            } catch (InputMismatchException e) {
                System.out.println("Could not parse a number. Please, try again");
                scanner.nextLine();
                return -1;
            }
        })
                .filter(n -> n > 0)
                .findFirst()
                .orElse(0);
        return numberLine;
    }

    private static Integer getAgePets(Scanner scanner) {
        int agePets = scanner.nextInt();
        if (agePets <= 0) {
            System.out.println("Incorrect input. Age <= 0");
            return null;
        }
        if (agePets > 10) {
            agePets++;
        }
        scanner.nextLine();
        return agePets;
    }

    private static String getPetsType(Scanner scanner) {
        System.out.println("Write pets type: ");
        String petsType = scanner.next().trim().toLowerCase();
        boolean isValidType = Stream.of("dog", "cat")
                .anyMatch(type -> type.equals(petsType));
        if (!isValidType) {
            System.out.println("Incorrect input. Unsupported pet type");
            scanner.nextLine();
            return null;
        }
        scanner.nextLine();
        return petsType;
    }
}
