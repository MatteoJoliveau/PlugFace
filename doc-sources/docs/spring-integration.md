---
id: spring-integration
title: Spring Integration
sidebar_label: Spring
---

If your application uses the [Spring Framework](https://spring.io/) you can use `plugface-spring` instead of `plugface-core`.

The Spring integration provides Spring-optimized classes and a Spring Boot Autoconfiguration that will automatically configure them as Beans.

The `SpringPluginContext` is a special plugin context that will lookup plugins in both the standard PlugFace context **AND** in the Spring application context.
So if we are requesting a plugin of type `Greeter` and the context cannot find it amongs plugins, it will look it up in Spring and return it, if it exists.

Also, the `SpringPluginManager` class will lookup plugin dependencies in the Spring context too, so you can inject regular Spring beens in plugins!  
See [Dependency Injection](dependency-injection.html) for more information.
### Example

```java
// Spring Configuration Class

@Bean
public Greeter greeter() {
    return new GreeterBean();
}
```
```java
// Somewhere

// it will return the Spring bean if a similar class is not found in the plugin context
Greeter greeter = manager.getPlugin(Greeter.class); 

```