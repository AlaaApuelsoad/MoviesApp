package com.alaa.MoviesApp.context;

public class UserContextHolder {

    private static final ThreadLocal<LoggedInUserContext> userContext = new ThreadLocal<>();

    public static void setLoggedInUserContext(LoggedInUserContext context) {
        userContext.set(context);
    }

    public static LoggedInUserContext getLoggedInUserContext() {
        return userContext.get();
    }

    public static void clearRequestContext(){
        userContext.remove(); //avoid memory leak
    }

}
