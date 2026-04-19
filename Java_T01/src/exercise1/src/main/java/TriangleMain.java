import java.util.Scanner;

import static java.lang.Math.sqrt;

public class TriangleMain {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        double x1 = readDouble(scanner, "put x1: ");
        double y1 = readDouble(scanner, "put y1: ");
        double x2 = readDouble(scanner, "put x2: ");
        double y2 = readDouble(scanner, "put y2: ");
        double x3 = readDouble(scanner, "put x3: ");
        double y3 = readDouble(scanner, "put y3: ");

        double a = searchCoordinate(x1, y1, x2, y2);
        System.out.println("A = " + a);
        double b = searchCoordinate(x3, y3, x2, y2);
        System.out.println("B = " + b);
        double c = searchCoordinate(x1, y1, x3, y3);
        System.out.println("C = " + c);
        double p = perimeterSum(a, b, c);
        if(isTriangle(a, b, c)) {
            System.out.printf("Perimeter: %.3f\n", p);
        } else {
            System.out.println("It's not a triangle");
        }

    }

    public static double readDouble(Scanner scanner, String prompt) {
        while (true) {
            try {
                System.out.println(prompt);
                return scanner.nextDouble();
            }
            catch (Exception e) {
                System.out.println("Could not parse a number. Please, try again");
                scanner.nextLine();
            }
        }
    }

    public static double searchCoordinate(double x1, double y1, double x2, double y2) {
        return Math.sqrt(Math.pow((x1 - x2), 2) + Math.pow((y1 - y2), 2));
    }

    public static double perimeterSum(double a, double b, double c) {
        return a + b + c;
    }

    public static boolean isTriangle(double a, double b, double c) {
        return (a + b > c) && (a + c > b) && (b + c > a);
    }
}

