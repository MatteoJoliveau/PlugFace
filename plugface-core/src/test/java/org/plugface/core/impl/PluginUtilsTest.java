package org.plugface.core.impl;

import org.junit.Test;
import org.plugface.core.annotations.Plugin;
import org.plugface.core.annotations.Require;

import java.util.Collection;

import static org.junit.Assert.*;

public class PluginUtilsTest {


    @Test
    public void shouldGetPluginName() {
        final String name = PluginUtils.getPluginName(new TestPlugin());
        assertEquals("test", name);
    }

    @Test
    public void shouldReturnNullIfNoNameIsProvided() {
        final String name = PluginUtils.getPluginName(new EmptyPlugin());
        assertNull(name);
    }

    @Test
    public void shouldDetectDependencies() {
        final boolean hasDependencies = PluginUtils.hasDependencies(DependentPlugin.class);
        assertTrue(hasDependencies);
    }

    @Test
    public void shouldReturnFalseOnNoDependencies() {
        final boolean hasDependencies = PluginUtils.hasDependencies(TestPlugin.class);
        assertFalse(hasDependencies);
    }

    @Test
    public void shouldRetrieveDependencies() {
        final Collection<PluginDependency> dependencies = PluginUtils.getDependencies(DependentPlugin.class);
        assertFalse(dependencies.isEmpty());
        System.out.println(dependencies);
        for (PluginDependency dependency : dependencies) {
            final Class<?> clazz = dependency.getDependencyClass();
            if (TestPlugin.class.equals(clazz)) {
                assertTrue(dependency.isOptional());
            } else if (EmptyPlugin.class.equals(clazz)) {
                assertFalse(dependency.isOptional());
            } else {
                fail("Incorrect class");
            }
        }
    }

    @Plugin("test")
    private static class TestPlugin {

    }

    @Plugin
    private static class EmptyPlugin {

    }

    @Plugin(value = "dependent")
    private static class DependentPlugin {


        private final TestPlugin dependency;
        private final EmptyPlugin empty;

        public DependentPlugin(@Require(optional = true) TestPlugin dependency, @Require EmptyPlugin empty) {
            this.dependency = dependency;
            this.empty = empty;
        }


    }
}