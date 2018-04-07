package org.plugface.core.internal;

import org.junit.Before;
import org.junit.Test;
import org.plugface.core.internal.di.Node;
import org.plugface.core.plugins.DependencyPlugin;
import org.plugface.core.plugins.OptionalPlugin;
import org.plugface.core.plugins.TestPlugin;
import org.plugface.core.annotations.Plugin;

import java.util.Collection;

import static org.junit.Assert.*;

public class AnnotationProcessorTest {

    private AnnotationProcessor sut;

    @Before
    public void setUp() throws Exception {
        sut = new AnnotationProcessor();
    }

    @Test
    public void shouldGetPluginName() {
        final String name = sut.getPluginName(new TestPlugin());
        assertEquals("test", name);
    }

    @Test
    public void shouldReturnNullIfNoNameIsProvided() {
        final String name = sut.getPluginName(new EmptyPlugin());
        assertNull(name);
    }

    @Test
    public void shouldDetectDependencies() {
        final boolean hasDependencies = sut.hasDependencies(DependencyPlugin.class);
        assertTrue(hasDependencies);
    }

    @Test
    public void shouldReturnFalseOnNoDependencies() {
        final boolean hasDependencies = sut.hasDependencies(TestPlugin.class);
        assertFalse(hasDependencies);
    }

    @Test
    public void shouldRetrieveDependencies() {
        final Collection<Node<?>> dependencies = sut.getDependencies(DependencyPlugin.class);
        assertFalse(dependencies.isEmpty());
    }

    @Plugin
    private static class EmptyPlugin {}
    

}