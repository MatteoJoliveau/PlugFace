package org.plugface.core.plugins;

import org.plugface.core.annotations.Plugin;

import javax.inject.Inject;

@Plugin("deep")
public class DeepDependencyPlugin {

    private final DependencyPlugin dependency;
    private final TestPlugin test;

    @Inject
    public DeepDependencyPlugin(DependencyPlugin dependency, TestPlugin test) {
        this.dependency = dependency;
        this.test = test;
    }
}
