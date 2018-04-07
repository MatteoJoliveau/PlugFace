package org.plugface.core.plugins;

import org.plugface.core.annotations.Plugin;

import javax.inject.Inject;

@Plugin("single")
public class SingleDependencyPlugin {

    private final OptionalPlugin test;

    @Inject
    public SingleDependencyPlugin(OptionalPlugin test) {
        this.test = test;
    }
}
