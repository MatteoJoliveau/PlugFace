package com.matteojoliveau.plugface;


public class NoSuchPluginException extends RuntimeException{

    private final String name;

    public NoSuchPluginException(String pluginName) {
        super("No plugin found for: " + pluginName);
        this.name = pluginName;
    }

    public NoSuchPluginException(String pluginName, Throwable cause) {
        super("No plugin found for: " + pluginName, cause);
        name = pluginName;
    }

    public NoSuchPluginException(String pluginName, String message, Throwable cause) {
        super(message, cause);
        this.name = pluginName;
    }
}
