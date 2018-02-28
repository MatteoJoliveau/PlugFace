package org.plugface.core.plugins;

import org.plugface.core.annotations.Plugin;
import org.plugface.core.annotations.Require;

@Plugin("deep")
public class DeepDependencyPlugin {

    private final DependencyPlugin dependency;
    private final TestPlugin test;

    public DeepDependencyPlugin(@Require DependencyPlugin dependency, @Require TestPlugin test) {
        this.dependency = dependency;
        this.test = test;
    }
}
