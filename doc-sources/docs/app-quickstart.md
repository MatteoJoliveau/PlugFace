---
id: app-quickstart
title: Applications Quickstart
sidebar_label: Applications Quickstart
---

PlugFace introduces a few concepts that are crucial to understand how to create and operate a plugin system.
First of all, what is a plugin in PlugFace?

A plugin is simply put a class that is not present in your application at compile time. PlugFace provides the primitives to
load arbitrary classes at runtime and instantiate them as singletons, allowing you to retrieve them and access their functionality
as if they were part of your system from the start.

You normally start by defining an SDK, for example a set of interfaces that plugins will implement. This set is known by
your application, and will be used as the bridge between the two worlds.  
Plugins will then implement these interfaces and be packaged separately.

Now, let's talk about the building blocks you need to use plugins in your application.

## Plugin Context
The context is your plugin registry. The [PluginContext](apidocs/plugface-core/org/plugface/core/PluginContext.html) interface defines the methods used to register, remove 
and retrieve plugin objects so that they can be used by your code. You normally don't need to access the context directly,
as the [PluginManager](#plugin-manager) will act as a safe proxy for it, but it is there if you need it.

## Plugin Manager
The manager is your connection point with the plugin world. The [PluginManager](apidocs/plugface-core/org/plugface/core/PluginManager.html) interface defines some proxy methods 
to access the context but also a `loadPlugins(PluginSource source)` method that is used to dinamically
load and instantiate plugin classes from a specified source.  
The default implementation is `DefaultPluginManager`, and a convenience factory method is provided if you don't need
to specify a particular implementation of its dependencies (`PluginContext`, `AnnotationProcessor` and `DependencyResolver`).  
Just call `PluginManagers.defaultPluginManager()` and it will create it for you.  
**It is recommended** that you keep the manager as a singleton and reuse everywhere in your application. 

## Plugin Sources
Plugins can be loaded from multiple sources.

The `PluginSource` interface abstract this mechanism away and allows to customize and extends the loading capabilities of PlugFace.

For convenience, a set of ready-to-use loaders is provided by the factory class `PluginSources`, such as loading from JAR file 
or from a list of `Class<>` objects (useful for testing or debugging).

# Example

First of all let's define our plugin SDK. As per tradition, we'll make an Hello World application.

We define our `Greeter` interface like so.  
**QUICK NOTE** Using interfaces is not actually recommended, as plugins can be retrieved from a String based name,
but it makes using them much easier since loading them without knowing their supertype only allows to use them via reflection.

```java
public interface Greeter { 

    // Greet the world
    String greet();
}
```

Somewhere in the universe we know that there is a plugin implementing this interface and it has been packaged in a convenient `greeter-plugin.jar` that we will put in a folder accessible from our application.

Now, let's load it in our application.

```java
// somewhere

PluginManager manager = PluginManagers.defaultPluginManager();
manager.loadPlugins(PluginSources.jarSource("path/to/plugin/folder"));

Greeter greeterPlugin = manager.getPlugin(Greeter.class); // Here is why having a shared interface between plugins and applications is convenient

Object greeterPlugin = manager.getPlugin("greeter"); // As an alternative if you don't have shared code, the name is defined by the plugin itself

System.out.println(greeterPlugin.greet()) // It works!
```

Et voilà, you have successfully modularized you application using plugins!  
Now let's write our actual greeter plugin.

 