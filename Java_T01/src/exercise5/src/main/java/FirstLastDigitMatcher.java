import java.util.Scanner;

public class FirstLastDigitMatcher {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int  size = scannerInputSize(scanner);
        if(size <= 0) {
            System.out.println("Input error. Size <= 0");
            return;
        }
        int[] numberList = inputNumberList(size);
        int[] newNumberList = firstLastDigit(numberList);
        if(newNumberList.length == 0) {
            System.out.println("There are no such elements");
        } else {
            for (int i = 0; i < newNumberList.length; i++) {
                System.out.print(newNumberList[i]);
                if (i < newNumberList.length - 1) {
                    System.out.print(" ");
                }
            }
        }

    }

    public static int scannerInputSize(Scanner scanner) {
        while (true) {
            System.out.println("Input size list");
            try{
               return scanner.nextInt();
            } catch (Exception e) {
                System.out.println("Could not parse a number. Please, try again");
                scanner.nextLine();
            }
        }
    }

    public static int[] inputNumberList(int size) {
        Scanner scanner = new Scanner(System.in);
        int[] numberList = new int[size];
        System.out.println("input numbers list");
        for (int i = 0; i < size; i++) {
            numberList[i] = scanner.nextInt();
        }
        return numberList;
    }

    public static int[] firstLastDigit(int[] numberList) {
        int count = 0;
        for(int item: numberList) {
            if(hasMatchingFirstLastDigit(item)) {
                count++;
            }
        }
        int[] result = new int[count];
        int index = 0;

        for (int item : numberList) {
            if (hasMatchingFirstLastDigit(item)) {
                result[index++] = item;
            }
        }

        return result;
    }

    public static boolean  hasMatchingFirstLastDigit(int number) {
        int abcNumber = Math.abs(number);
        if(abcNumber < 10) {
            return true;
        }

        int lastDigit = abcNumber % 10;
        int firstDigit = abcNumber;
        while(firstDigit >= 10) {
            firstDigit /= 10;
        }
        return firstDigit == lastDigit;
    }
}
