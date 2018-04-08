package org.plugface.core.plugins;

import org.plugface.core.annotations.Plugin;

@Plugin("test")
public class TestPlugin {

    public String greet() {
        return "Hello PlugFace!";
    }
}
