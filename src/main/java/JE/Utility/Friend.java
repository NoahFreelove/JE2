package JE.Utility;

public class Friend {
    private final String parent;
    private final String friend;
    private final RunnableArg callback;
    public Friend(String friend, RunnableArg callback){
        StackTraceElement ste = Thread.currentThread().getStackTrace()[2];
        this.parent = ste.getClassName();
        this.friend = friend;
        this.callback = callback;
    }
    public void call(Object... args){
        StackTraceElement stackTrace = Thread.currentThread().getStackTrace()[2];
        String callerClass = stackTrace.getClassName();
        if(callerClass.equals(parent) || callerClass.equals(friend)){
            callback.run(args);
        }

    }
}
