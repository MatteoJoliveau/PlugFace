package org.plugface.core.internal;

import org.junit.Before;
import org.junit.Test;
import org.plugface.core.internal.tree.DependencyNode;
import org.plugface.core.plugins.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;

public class DependencyResolverTest {

//    private AnnotationProcessor mockProcessor = mock(AnnotationProcessor.class);
    private AnnotationProcessor mockProcessor = new AnnotationProcessor();
    private DependencyResolver sut;

    @Before
    public void setUp() throws Exception {
        sut = new DependencyResolver(mockProcessor);
    }

    @Test
    public void shouldResolveComplexDependencies() throws Exception {
        List<Class<?>> plugins = newArrayList(DeepDependencyPlugin.class, DependencyPlugin.class, TestPlugin.class, OptionalPlugin.class, SingleDependencyPlugin.class);
        Collection<DependencyNode> nodes = sut.resolve(plugins);
        assertEquals(plugins.size(), nodes.size());
        for (DependencyNode node : nodes) {
            assertTrue(plugins.contains(node.getDependencyClass()));
        }
    }


    private <T> List<T> newArrayList(T... elements) {
        ArrayList<T> list = new ArrayList<>(elements.length);
        list.addAll(Arrays.asList(elements));
        return list;
    }


}