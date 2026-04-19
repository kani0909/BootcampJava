import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class StringScannerLine {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        List<String> stringList = stringInput(scanner);

        String subString = scanner.nextLine();

        List<String> filterResult = filterString(stringList, subString);

        for (int i = 0; i < filterResult.size(); i++) {
            System.out.print(filterResult.get(i));
            if(i < filterResult.size() - 1) {
                System.out.print(", ");
            }

        }

        scanner.close();
    }

    public static List<String> stringInput(Scanner scanner){
        System.out.println("Put string number");
        int numberLine = scanner.nextInt();
        if (numberLine <= 0) {
            System.out.println("error");
        }
        scanner.nextLine();
        List<String> stringList = new ArrayList<>();
        System.out.println("Put Strings: ");
        for (int i = 0; i < numberLine; i++) {
            String line =  scanner.nextLine();
            stringList.add(line);
        }
        return stringList;
    }

    public static List<String> filterString(List<String> stringList, String subString) {
        List<String> result = new ArrayList<>();
        for (String str: stringList) {
            if(containsMy(str, subString)) {
                result.add(str);
            }
        }
        return result;
    }

    public static Boolean containsMy(String str, String subStr) {
        if(subStr == null || subStr.isEmpty()) {
            return true;
        }
        if (str == null) {
            return false;
        }

        int strLen = str.length();
        int subLen = subStr.length();

        for (int i = 0; i < strLen - subLen; i++) {
            int j;
            for (j = 0; j < subLen; j++) {
                if (str.charAt(i + j) != subStr.charAt(j)) {
                    break;
                }
            }
            if (j == subLen) {
                return true;
            }
        }
        return false;
    }
}
