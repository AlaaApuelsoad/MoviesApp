package com.fawry.MoviesApp.filters;

public class RequestContext {

    private static final ThreadLocal<String> requestContext = new ThreadLocal<>();

    public static void setRequestContext(String requestContext) {
        RequestContext.requestContext.set(requestContext);
    }

    public static String getRequestContext() {
        return requestContext.get();
    }

    public static void clearRequestContext(){
        requestContext.remove(); //avoid memory leak
    }

    public static String generateUUID(){
        return java.util.UUID.randomUUID().toString();
    }
}
