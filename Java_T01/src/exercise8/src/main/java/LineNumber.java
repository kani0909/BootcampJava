import java.util.Scanner;

public class LineNumber {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        int count = 0;
        int previous = 0;
        int current = 0;
        boolean isOrdered = true;
        int errorPosition = 0;
        boolean reading = true;

        while (reading) {
            if (!scanner.hasNext()) {
                reading = false;
            }

            if (scanner.hasNextInt()) {
                current = scanner.nextInt();
                count++;

                if (count > 1) {
                    if (current <= previous && isOrdered) {
                        isOrdered = false;
                        errorPosition = count;
                    }
                }

                previous = current;
            } else {
                scanner.next();
                reading = false;
            }
        }

        scanner.close();

        if (count == 0) {
            System.out.println("Input error");
        } else if (isOrdered) {
            System.out.println("The sequence is ordered in ascending order");
        } else {
            System.out.println("The sequence is not ordered from the ordinal number of the number " + errorPosition);
        }
    }
}
