package org.plugface.core.plugins;

import org.plugface.core.annotations.Plugin;

import javax.inject.Inject;

@Plugin(value = "dependency")
public class DependencyPlugin {

    private final TestPlugin dependency;
    private final OptionalPlugin optional;

    @Inject
    public DependencyPlugin(TestPlugin dependency, OptionalPlugin optional) {
        this.dependency = dependency;
        this.optional = optional;
    }

    public String test() {
        return String.format("Dependency: %s\nOptional: %s", dependency.greet(), optional != null ? optional.greet() : null);
    }


}