#PlugFace Framework

**PLUGFACE IS STILL IN EARLY DEVELOPMENT. WAIT FOR THE 1.0.0-RELEASE FOR PRODUCTION USE** 

PlugFace is a simple, lightweight, high abstraction plugin framework for Java applications. It focuses on simplicity, easy and clean API and modularity.

## Core Concepts
* Simple `Plugin` interface that provides a unified API to start, stop and configure plugins
* `PluginManager` utility class to load, configure and register plugins. It should be the primary way for applications to manage the PlugFace echosystems.
* A `PlugfaceContext` that acts as a repository for registered plugins and managers. It holds the reference to all `PluginManager` instances and to the plugins that have been registered.

### Download
PlugFace is currently distributed from a custom repository. To download the latest snapshot in Maven, add the following snippet to the `repositories` section of your `pom.xml` file:

```xml
<repository>
    <id>plugface-snapshots</id>
    <name>PlugFace Snapshot Repository</name>
    <url>https://nexus.matteojoliveau.com/repository/maven-snapshots/</url>
</repository>
```

Then add your dependency as usual:
```xml
<dependency>
    <groupId>com.matteojoliveau.plugface</groupId>
    <artifactId>plugface-core</artifactId>
    <version>0.1.0-SNAPSHOT</version>
</dependency>
```

For Gradle, add the following lines to `build.gradle`:
```gradle
repositories {
    maven {
        url "https://nexus.matteojoliveau.com/repository/maven-snapshots/"
    }
}
```
Then add:
```gradle
dependencies {
    compile 'com.matteojoliveau.plugface:plugface-core:0.1.0-SNAPSHOT'
}
```

### Example usage
Let's consider a simple Plugin implementation:
```java
package com.matteojoliveau.test;

import com.matteojoliveau.plugface.PlugfaceContext;
import com.matteojoliveau.plugface.Plugin;
import com.matteojoliveau.plugface.PluginConfiguration;

public class Test implements Plugin{
    
    private String name;
    private PluginConfiguration pluginConfiguration;
    private PlugfaceContext context;
    
    public Test() {
        this.name = "testPlugin";
    }

    public void start() {
        System.out.println("Hello I am a test plugin!");
    }

    public void stop() {
        System.out.println("The test plugin is stopping...");
    }

    public PluginConfiguration getPluginConfiguration() {
        return pluginConfiguration;
    }

    public void setPluginConfiguration(PluginConfiguration pluginConfiguration) {
      this.pluginConfiguration = pluginConfiguration;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
      this.name = name;
    }

    public PlugfaceContext getContext() {
        return context;
    }
    
    public void setContext(PlugfaceContext context) {
        this.context = context;
    }
}
```

Now let's simulate an application that loads the plugins from a folder and starts the test plugin:

```java
  //Somewhere in the application
  PlugfaceContext context = new DefaultPlugfaceContext();

  PluginManager manager = new PluginManager("managerOne", context);

  manager.setPluginFolder("/path/to/plugins/folder/");
  List<Plugin> loaded = null;
  try {
      loaded = manager.loadPlugins();
  } catch (IOException | ClassNotFoundException | InstantiationException | IllegalAccessException e) {
      e.printStackTrace();
  }

  for (Plugin plugin: loaded) {
      manager.registerPluginInContext(plugin.getName(), plugin);
  }

  context.getPlugin("testPlugin").start();
```
You should see a wonderful *Hello I am a test plugin!* printing out. PlugFace is working!
    
### License
Copyright 2017 Matteo Joliveau

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
