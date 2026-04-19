import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        List<User> userList = new ArrayList<>();
        int numberLine = getNumberOfUsers(scanner);

        for (int i = 0; i < numberLine; i++) {
            String name = getUserName(scanner);
            Integer age = getUserAge(scanner);
            userList.add(new User(name, age));
        }
        List<String> adultUsers = userList.stream()
                .filter(user -> user.getAge() != null && user.getAge() >= 18)
                .map(User::getName)
                .toList();
        String result = String.join(", ", adultUsers);
        System.out.println(result);
        scanner.close();
    }

    private static String getUserName(Scanner scanner) {
        String name = "";
        boolean validName = false;
        while (!validName) {
            System.out.println("Put name: ");
            name = scanner.nextLine().trim();
            if(name.isEmpty()) {
                System.out.println("The name cannot be empty. Try again");
            } else {
                validName= true;
            }
        }
        return name;
    }

    private static Integer getUserAge(Scanner scanner) {
        Integer age = null;
        boolean validAge = false;
        while (!validAge) {
            try {
                System.out.println("put age: ");
                age = scanner.nextInt();
                scanner.nextLine();
                if (age <= 0) {
                    System.out.println("Invalid input. Age <= 0");
                    return null;
                }
                else {
                    validAge = true;
                }
            } catch (Exception e) {
                System.out.println("Failed to process number. Please try again");
                scanner.nextLine();
            }
        }
        return age;
    }

    private static int getNumberOfUsers(Scanner scanner) {
        int numberLine = 0;
        boolean validInput = false;
        while (!validInput) {
            try {
                System.out.println("Put number");
                numberLine = scanner.nextInt();
                scanner.nextLine();
                if (numberLine <= 0) {
                    System.out.println("Error, try again!");
                } else {
                    validInput = true;
                }

            } catch (Exception e) {
                System.out.println("Incorrect");
                scanner.nextLine();
            }
        }
        return numberLine;
    }


}
