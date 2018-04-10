---
id: dependency-injection
title: Dependency Injection
sidebar_label: Dependency Injection
---

Small plugins are handy to offload simple tasks outside your application, but for more complicated scenarios it may be useful to be able to combine them.

PlugFace provides a full fledged dependency injection system based on topological sorting that can resolve deep-nested dependencies, allowing plugins to have other fellow plugins injected in their constructor. PlugFace uses the standard Java `@Inject` annotation so your classes can still be used with any regular dependency injection system.

## Spring Integration

As pointed out in the [Spring Integration](spring-integration.html) section, if using `plugface-spring` you can also inject Spring beans into your plugins. 
This can be useful in enterprise application where you want to access services inside your plugins to modularize business logic, or to access database by injecting DAO classes
or repositories.

### Example



```java
// Simple Plugin
package plugins;

import org.plugface.core.annotations.Plugin;

@Plugin(name = "test")
public class TestPlugin {

    public String greet() {
        return "Hello PlugFace!";
    }
}
```

```java
// Dependent Plugin

package plugins;

import org.plugface.core.annotations.Plugin;

import javax.inject.Inject;

@Plugin(name = "single")
public class SingleDependencyPlugin {

    private final TestPlugin test;

    @Inject
    public SingleDependencyPlugin(TestPlugin test) {
        this.test = test;
    }

    public TestPlugin getTest() {
        return test;
    }
}

```

```java
// Usage

TestPlugin test = manager.getPlugin("test");
SingleDependencyPlugin sdp = manager.getPlugin("single");

test.equals(sdp.getTest()); // True. They are the same instance as plugins are registered as singletons
```