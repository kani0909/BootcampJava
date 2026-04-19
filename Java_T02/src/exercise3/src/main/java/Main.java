import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int numberLine = getValidNumber(scanner);

        List <Animal> pets = new ArrayList<>();
        for (int i = 0; i < numberLine; i++) {
            String petsType = getPetsType(scanner);
            if (petsType == null) continue;
            System.out.println("name:");
            String namePets = scanner.next();
            scanner.nextLine();
            System.out.println("age");
            int agePets = scanner.nextInt();
            if (agePets <= 0) {
                System.out.println("Incorrect input. Age <= 0");
                continue;
            }
            scanner.nextLine();

            petTypeValid(petsType, pets, namePets, agePets);

        }
        for (Animal pet : pets) {
            if (pet instanceof Herbivore) {
                System.out.println(pet);
            }
        }
        for (Animal pet : pets) {
            if (pet instanceof Omnivore) {
                System.out.println(pet);
            }
        }
        scanner.close();
    }

    private static int getValidNumber(Scanner scanner) {
        int numberLine = 0;
        while (true) {
            System.out.println("Put pets number");
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
        return numberLine;
    }

    private static void petTypeValid(String petsType, List<Animal> pets, String namePets, int agePets) {
        if ("guinea".equals(petsType)) {
            pets.add(new GuineaPig(namePets, agePets));
        }
        else if ("hamster".equals(petsType)) {
            pets.add(new Hamster(namePets, agePets));
        } else  if ("dog".equals(petsType)) {
            pets.add(new Dog(namePets, agePets));
        } else {
            pets.add(new Cat(namePets, agePets));
        }
    }

    private static String getPetsType(Scanner scanner) {
        System.out.println("Write pets type");
        String petsType = scanner.next().trim().toLowerCase();
        scanner.nextLine();
        if(!"dog".equals(petsType) && !"cat".equals(petsType)
                && !"hamster".equals(petsType) && !"guinea".equals(petsType)) {
            System.out.println("Incorrect input. Unsupported pet type");
            return null;
        }
        return petsType;
    }

}
