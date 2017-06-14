import java.awt.*;
import java.util.ArrayList;

/**
 * Created by yangj on 2017/6/11.
 */
public class ErrorHandler {
    public static ArrayList<String> errorList = new ArrayList<>();
    public static ArrayList<String> warningList = new ArrayList<>();

    public static void error(int loc, String msg) {
        error(String.valueOf(loc) + ": " + msg);
    }

    public static void error(String msg) {
        errorList.add(msg);
    }

    public static int errorNum() {
        return errorList.size();
    }

    public static void printError() {
        System.out.println("Error:");
        for (int i = 0; i < errorNum(); i++)
            System.out.println("line " + errorList.get(i).toString());
    }

    public static void warning(int loc, String msg) {
        warning(String.valueOf(loc) + ": " + msg);
    }

    public static void warning(String msg) {
        warningList.add(msg);
    }

    public static int warningNum() {
        return warningList.size();
    }

    public static void printWarning() {
        System.out.println("Warning:");
        for (int i = 0; i < warningNum(); i++)
            System.out.println("line " + warningList.get(i).toString());
    }

    public static boolean hasError() {
        return errorList.size() > 0;
    }

    public static boolean hasWarning() {
        return warningList.size() > 0;
    }

}
