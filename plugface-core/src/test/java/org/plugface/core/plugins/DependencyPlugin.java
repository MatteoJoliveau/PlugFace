package org.plugface.core.plugins;

import org.plugface.core.annotations.Plugin;
import org.plugface.core.annotations.Require;

@Plugin(value = "dependency")
public class DependencyPlugin {

    private final TestPlugin dependency;
    private final OptionalPlugin optional;

    public DependencyPlugin(@Require TestPlugin dependency, @Require(optional = true) OptionalPlugin optional) {
        this.dependency = dependency;
        this.optional = optional;
    }

    public String test() {
        return String.format("Dependency: %s\nOptional: %s", dependency.greet(), optional != null ? optional.greet() : null);
    }


}