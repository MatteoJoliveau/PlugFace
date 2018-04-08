package org.plugface.core.internal.di;

import java.util.Objects;

public class Node<T> {
    private final Class<T> refClass;

    public Node(Class<T> refClass) {
        this.refClass = refClass;
    }

    public Class<T> getRefClass() {
        return refClass;
    }

    @Override
    public String toString() {
        return "Node{" +
                "class=" + refClass.getSimpleName() +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Node)) {
            return false;
        }
        Node<?> node = (Node<?>) o;
        return Objects.equals(refClass, node.refClass);
    }

    @Override
    public int hashCode() {
        return Objects.hash(refClass);
    }
}
