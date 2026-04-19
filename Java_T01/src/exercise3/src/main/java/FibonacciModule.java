import java.util.InputMismatchException;
import java.util.Scanner;

public class FibonacciModule {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            try {
                int n = getValidInput(scanner);

                if (n > 92) {
                    System.out.println("Too large n");
                    continue;
                }

                long result = fibonacci(n);
                System.out.println("F(" + n + ") = " + result);

                System.out.print("Do you want to continue? (yes/no): ");
                String response = scanner.next();
                if (!response.equalsIgnoreCase("yes")) {
                    break;
                }

            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
            }
        }

        scanner.close();
    }

    public static long fibonacci(int n) {
        if (n < 0) {
            throw new IllegalArgumentException("n must be non-negative");
        }
        if (n <= 1) {
            return n;
        }

        if (n > 92) {
            throw new IllegalArgumentException("Too large n, overflow will occur");
        }

        return fibonacciHelper(n);
    }

    private static long fibonacciHelper(int n) {
        if (n <= 1) {
            return n;
        }
        return fibonacciHelper(n - 1) + fibonacciHelper(n - 2);
    }

    public static int getValidInput(Scanner scanner) {
        while (true) {
            try {
                System.out.print("Enter the index of the Fibonacci number: ");
                return scanner.nextInt();
            } catch (InputMismatchException e) {
                System.out.println("Failed to process the number. Please try again");
                scanner.nextLine();
            }
        }
    }

}