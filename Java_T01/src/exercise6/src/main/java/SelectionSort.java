import java.util.Scanner;

public class SelectionSort {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int size = scannerInputSize(scanner);
        if (size <= 0) {
            System.out.println("Input error. Size <= 0");
            return;
        }
        double[] numberList = inputNumberOfList(scanner, size);
        double[] newArray = arraySort(numberList);
        for (int i = 0; i < newArray.length; i++) {
            System.out.print(newArray[i]);
            if (i < newArray.length - 1) {
                System.out.print(" ");
            }

        }
    }
    public static int scannerInputSize(Scanner scanner) {
        while (true) {
            System.out.println("Input size number");
            try{
                return scanner.nextInt();
            } catch (Exception e) {
                System.out.println("Could not parse a number. Please, try again");
                scanner.nextLine();
            }
        }
    }

    public static double[] inputNumberOfList(Scanner scanner, int size) {
        double[] numberList = new double[size];
        System.out.println("put list");
        for (int i = 0; i < size; i++) {
            numberList[i] = scanner.nextDouble();
        }
        return numberList;
    }

    public static double[] arraySort(double[] numberList){
        for (int i = 0; i < numberList.length - 1; i++) {
            int minIndex = i;
            for (int j = i + 1; j < numberList.length; j++) {
                if(numberList[j] < numberList[minIndex]) {
                    minIndex = j;
                }
            }
            double temp = numberList[minIndex];
            numberList[minIndex] = numberList[i];
            numberList[i] = temp;
        }
        return numberList;
    }
}

