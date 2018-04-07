package org.plugface.core.plugins;

import org.plugface.core.annotations.Plugin;

@Plugin("optional")
public class OptionalPlugin {

    public String greet() {
        return "Hello PlugFace!";
    }
}