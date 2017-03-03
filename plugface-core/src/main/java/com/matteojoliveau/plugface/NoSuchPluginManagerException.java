package com.matteojoliveau.plugface;


public class NoSuchPluginManagerException extends RuntimeException{

    private final String name;

    public NoSuchPluginManagerException(String managerName) {
        super("No plugin manager found for: " + managerName);
        this.name = managerName;
    }

    public NoSuchPluginManagerException(String managerName, Throwable cause) {
        super("No plugin manager found for: " + managerName, cause);
        name = managerName;
    }

    public NoSuchPluginManagerException(String managerName, String message, Throwable cause) {
        super(message, cause);
        this.name = managerName;
    }
}
