package org.plugface.core;

import org.plugface.core.annotations.Plugin;
import org.plugface.core.annotations.Require;

public class PluginTest {


    @Plugin(value = "test")
    public static class TestPlugin {

        private final DependencyPlugin dependency;
        private final OptionalPlugin optional;

        public TestPlugin(@Require DependencyPlugin dependency, @Require(optional = true) OptionalPlugin optional) {
            this.dependency = dependency;
            this.optional = optional;
        }

        public String test() {
            return String.format("Dependency: %s\nOptional: %s", dependency.greet(), optional != null ? optional.greet() : null);
        }


    }

    @Plugin("dependency")
    public static class DependencyPlugin {

        public String greet() {
            return "Hello PlugFace!";
        }
    }

    @Plugin("optional")
    public static class OptionalPlugin {
        public String greet() {
            return "Hello PlugFace!";
        }
    }
}
