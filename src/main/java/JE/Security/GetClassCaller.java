package JE.Security;

public class GetClassCaller {
    public static String getCallerClass() {
        return Thread.currentThread().getStackTrace()[3].getClassName();
    }
    public static String getCallerClass(int depth) {
        return Thread.currentThread().getStackTrace()[3+depth].getClassName();
    }
}
