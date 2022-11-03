package JE.Security;

public class GetClassCaller {
    public static String getCallerClass() {
        return Thread.currentThread().getStackTrace()[3].getClassName();
    }
}
