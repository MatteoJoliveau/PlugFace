package org.plugface.core.plugins;

import org.plugface.core.annotations.Plugin;
import org.plugface.core.annotations.Require;

@Plugin("single")
public class SingleDependencyPlugin {

    private final OptionalPlugin test;

    public SingleDependencyPlugin(@Require(optional = true) OptionalPlugin test) {
        this.test = test;
    }
}
