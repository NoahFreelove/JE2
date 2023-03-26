package org.JE.JE2.Utility;

public class Friend {
    private final String parent;
    private final String friend;
    private final RunnableArg callback;
    private final Object[] obj;

    public Friend(String friend, RunnableArg callback){
        StackTraceElement ste = Thread.currentThread().getStackTrace()[2];
        this.parent = ste.getClassName();
        this.friend = friend;
        this.callback = callback;
        this.obj = null;
    }

    public Friend(String friend, Object[] objects){
        StackTraceElement ste = Thread.currentThread().getStackTrace()[2];
        this.parent = ste.getClassName();
        this.friend = friend;
        this.callback = null;
        this.obj = objects;
    }

    public Object[] get(){
        if(obj == null)
            return new Object[]{};
        StackTraceElement stackTrace = Thread.currentThread().getStackTrace()[2];
        String callerClass = stackTrace.getClassName();
        if(callerClass.equals(parent) || callerClass.equals(friend)){
            return obj;
        }
        return new Object[]{};
    }

    public void call(Object... args){
        StackTraceElement stackTrace = Thread.currentThread().getStackTrace()[2];
        String callerClass = stackTrace.getClassName();
        if(callerClass.equals(parent) || callerClass.equals(friend)){
            if(callback !=null)
                callback.run(args);
        }

    }
}
