package org.plugface.core;

import java.util.Objects;

public final class PluginRef<T> {

    private final T ref;
    private final String name;
    private final Class<T> type;

    private PluginRef(T ref, String name, Class<T> type) {
        this.ref = Objects.requireNonNull(ref, "Plugin reference cannot be null");
        this.name = Objects.requireNonNull(name, "Plugin name cannot be null");
        this.type = Objects.requireNonNull(type, "Plugin type cannot be null");
    }

    @SuppressWarnings("unchecked")
    public static <T> PluginRef<T> of(T ref, String name) {
        return new PluginRef<>(ref, name, (Class<T>) ref.getClass());
    }

    public T get() {
        return ref;
    }

    public String getName() {
        return name;
    }

    public Class<T> getType() {
        return type;
    }


}
