import java.util.Scanner;

public class NegativeAverageCalculator {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int size = scannerInputSecond(scanner);
        if(size <= 0) {
            System.out.println("Input error. Size <= 0");
            return;
        }
        int[] numberList = numberOfList(size);
        double averageSum = sumAverageCalc(numberList);
        System.out.println("Average og negative numbers: "+ averageSum);
        scanner.close();

    }

    public static int scannerInputSecond(Scanner scanner) {
        while (true) {
            System.out.println("Put  size");
            try {
                return scanner.nextInt();
            } catch (Exception e) {
                System.out.println("Could not parse a number. Please, try again");
                scanner.nextLine();
            }
        }
    }

    public static int[] numberOfList(int size){
        Scanner scanner = new Scanner(System.in);
        int[] numberList = new int[size];
        System.out.println("Input number List");
        for (int i = 0; i < size; i++) {
            numberList[i] = scanner.nextInt();
        }
        return numberList;
    }

    public static double sumAverageCalc(int[] number){
        int sum = 0;
        int count = 0;

        for (int item: number) {
            if(item < 0) {
                sum += item;
                count++;
            }
        }
        if (count == 0) {
            System.out.println("There are no negative elements");

        }
        return count == 0 ? 0 : (double) sum / count;
    }

}
