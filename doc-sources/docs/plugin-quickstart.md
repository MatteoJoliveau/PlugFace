---
id: plugin-quickstart
title: Plugin Quickstart
sidebar_label: Plugin Quickstart
---

If you followed the [Application Quickstart](application-quickstart.md) then you have already got a grasp on how plugins work in PlugFace.

In the previous quickstart we imported a greeter plugin but we didn't cover how this plugin was actually made (because our application doesn't need to know it).

Let's write it then. In a different project we will import our application SDK (i.e. the `Greeter` interface) and create a single class.

```java
package my.app.plugins;

import org.plugface.core.annotations.Plugin;
import my.app.interfaces.Greeter;

@Plugin(name = "greeter") // we define that this is a plugin and it has the name "greeter"
public class GreeterPlugin implements Greeter {

    @Override
    public String greet() {
        return "Hello PlugFace!";
    }
}
```

It's as simple as that. We will now package our plugin in a JAR file (for example `greeter-plugin.jar`) and copy it into some folder for our application to consume.

## Dependency Injection

Plugins can declare dependencies between each other. See the [Dependency Injection guide](dependency-injection.md) for more detailed examples.