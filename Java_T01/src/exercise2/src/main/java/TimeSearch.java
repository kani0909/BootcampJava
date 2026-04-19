import java.util.Scanner;

public class TimeSearch {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        int number = scannerInputSecond(scanner, "input second: ");

        if (number < 0) {
            System.out.println("Incorrect time");
        } else {
            int h = convertToHours(number);
            int m = convertToMinute(number);
            int s = convertToSecond(number);
            System.out.printf("%d:%02d:%02d\n", h, m, s);
        }
    }

    public static int convertToHours(int number) {
        return number / 3600;
    }

    public static int convertToMinute(int number) {
        return (number % 3600) / 60;
    }

    public static int convertToSecond(int number) {
        return number % 60;
    }

    public static int scannerInputSecond(Scanner scanner, String prompt) {
        while (true) {
            try {
                System.out.println(prompt);
                return scanner.nextInt();
            } catch (Exception e) {
                System.out.println("Could not parse a number. Please, try again");
                scanner.nextLine();
            }
        }
    }
}

