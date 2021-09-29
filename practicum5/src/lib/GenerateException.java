package lib;

public class GenerateException {
    public static void printError(String className, Exception err) {
        String methodStr = className + " geeft een error:\n";

        StackTraceElement[] errStack = err.getStackTrace();
        String errStackString = "";

        for (StackTraceElement stackTraceElement : errStack) {
            errStackString += "    " + stackTraceElement + "\n";
        }

        System.err.println("\n" + methodStr
        + "  " + err.getMessage() + "\n"
        + errStackString );
    }
}
