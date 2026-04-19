import java.io.*;
import java.util.Scanner;

public class FileReader {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter file path: ");
        String filePath = scanner.nextLine();
        scannerFileReader(filePath);
    }

    public static void scannerFileReader(String filePath) {
        File file = new File(filePath);

        if (!file.exists()) {
            System.out.println("Input error. File doesn't exist");
            return;
        }

        try (Scanner fileScanner = new Scanner(new File(filePath))) {
            int size = 0;

            if (fileScanner.hasNextLine()) {
                String firstLine = fileScanner.nextLine().trim();

                try {
                    size = Integer.parseInt(firstLine);
                } catch (NumberFormatException e) {
                    System.out.println("Input error. Invalid size format");
                    return;
                }
            } else {
                System.out.println("Input error. File is empty");
                return;
            }

            if (size <= 0) {
                System.out.println("Input error. Size <= 0");
                return;
            }

            double[] numberList = new double[size];
            int actualCount = 0;

            if (fileScanner.hasNextLine()) {
                actualCount = getActualCount(fileScanner, actualCount, size, numberList);
                Scanner lineScanner;
                String numbersLine;

                actualCount = getCount(actualCount, size, fileScanner, numberList);
            }

            if (actualCount < size) {
                System.out.println("Input error. Insufficient number of elements");
                return;
            }

            System.out.println(size + "  ");
            extracted(actualCount, numberList);

        } catch (FileNotFoundException e) {
            System.out.println("Input error. File doesn't exist");
        }
    }

    private static void extracted(int actualCount, double[] numberList) {
        for (int i = 0; i < actualCount; i++) {
            System.out.print(numberList[i]);
            if (i < actualCount - 1) {
                System.out.print(" ");
            }
        }
        System.out.print("\n");

        double[] maxMin = maxAndMinElement(numberList);
        saveToFile(maxMin[0], maxMin[1]);
        System.out.println("Saving min and max values in file");

        System.out.println("Saved to result.txt: " + maxMin[0] + " " + maxMin[1]);
    }

    private static int getCount(int actualCount, int size, Scanner fileScanner, double[] numberList) {
        String numbersLine;
        Scanner lineScanner;
        while (actualCount < size && fileScanner.hasNextLine()) {
            numbersLine = fileScanner.nextLine().trim();
            if (!numbersLine.isEmpty()) {
                lineScanner = new Scanner(numbersLine);
                while (lineScanner.hasNext() && actualCount < size) {
                    if (lineScanner.hasNextDouble()) {
                        double num = lineScanner.nextDouble();
                        numberList[actualCount] = num;
                        actualCount++;
                    } else {
                        lineScanner.next();
                    }
                }
                lineScanner.close();
            }
        }
        return actualCount;
    }

    private static int getActualCount(Scanner fileScanner, int actualCount, int size, double[] numberList) {
        String numbersLine = fileScanner.nextLine().trim();

        Scanner lineScanner = new Scanner(numbersLine);

        while (lineScanner.hasNext() && actualCount < size) {
            if (lineScanner.hasNextDouble()) {
                double num = lineScanner.nextDouble();
                numberList[actualCount] = num;
                actualCount++;
            } else {
                String skipped = lineScanner.next();
            }
        }
        lineScanner.close();
        return actualCount;
    }

    public static double[] maxAndMinElement(double[] numberList) {
        double min = numberList[0];
        double max = numberList[0];

        for (int i = 1; i < numberList.length; i++) {
            if (numberList[i] < min) {
                min = numberList[i];
            }
            if (numberList[i] > max) {
                max = numberList[i];
            }
        }

        return new double[]{min, max};
    }

    public static void saveToFile(double min, double max) {
        try (PrintWriter writer = new PrintWriter("result.txt")) {
            writer.println(min + " " + max);
        } catch (FileNotFoundException e) {
            System.out.println("Error saving to file: " + e.getMessage());
        }
    }
}