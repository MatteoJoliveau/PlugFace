# PlugFace Framework
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)
[![Version](https://img.shields.io/badge/Snapshot-v0.2.0--SNAPSHOT-green.svg)](https://nexus.matteojoliveau.com/#browse/browse/components:maven-snapshots)
[![GitHub release](https://img.shields.io/badge/Release-v0.2.0-blue.svg)](https://github.com/MatteoJoliveau/PlugFace/releases/latest)

**PLUGFACE IS STILL IN EARLY DEVELOPMENT. WAIT FOR THE 1.0.0-RELEASE FOR PRODUCTION USE** 

PlugFace is a simple, lightweight, high abstraction plugin framework for Java applications. It focuses on simplicity, easy and clean API and modularity.

## Core Concepts
* Simple `Plugin` interface that provides a unified API to start, stop and configure plugins
* `PluginManager` utility class to load, configure and register plugins. It should be the primary way for applications to manage the PlugFace echosystems.
* A `PlugfaceContext` that acts as a repository for registered plugins and managers. It holds the reference to all `PluginManager` instances and to the plugins that have been registered.

### Download
PlugFace is currently distributed from a custom repository. 
To download the latest stable release in Maven, add the following snippet to the `repositories` section of your `pom.xml` file:

```xml
<repository>
    <id>plugface-releases</id>
    <name>PlugFace Releas Repository</name>
    <url>https://nexus.matteojoliveau.com/repository/maven-releases/</url>
</repository>
```

Then add your dependency as usual:
```xml
<dependency>
    <groupId>com.matteojoliveau.plugface</groupId>
    <artifactId>plugface-core</artifactId>
    <version>0.2.0-RELEASE</version>
</dependency>
```

If you want to try the latest dev build, you can use the snapshot repository.
To download the latest snapshot in Maven, add the following snippet to your `pom.xml` file:

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
    <version>0.2.0-SNAPSHOT</version>
</dependency>
```

For Gradle, add the following lines to `build.gradle`:
```gradle
repositories {
    maven {
        url "https://nexus.matteojoliveau.com/repository/maven-releases/"
    }
}
```
Then add:
```gradle
dependencies {
    compile 'com.matteojoliveau.plugface:plugface-core:0.2.0-RELEASE'
}
```

### Example usage
Let's consider a simple Plugin implementation:
```java
package com.matteojoliveau.test;

import com.matteojoliveau.plugface.PlugfaceContext;
import com.matteojoliveau.plugface.Plugin;
import com.matteojoliveau.plugface.PluginConfiguration;
import com.matteojoliveau.plugface.impl.DefaultPluginConfiguration;

public class Test implements Plugin{
    
    private String name;
    private PluginConfiguration pluginConfiguration;
    private PlugfaceContext context;
    
    public Test() {
        this.name = "testPlugin";
        this.pluginConfiguration = new DefaultPluginConfiguration();
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

PluginManager manager = new PluginManager(context);
manager.setPluginFolder("/path/to/plugins");
manager.setPermissionsFile("/path/to/permissions.properties"); //optional
List<Plugin> loaded = null;
try {
    loaded = manager.loadPlugins();
} catch (IOException e) {
    e.printStackTrace();
}

for (Plugin p : loaded) {
    context.addPlugin(p.getName(), p);
}

manager.startAll(); 
```
You should see a wonderful *Hello I am a test plugin!* printing out. PlugFace is working!

### Plugins Permissions
PlugFace implements a sandbox to isolate Plugins from the application's environment. By default, Plugins run without any permission, so executing actions like trying to access files or the network will result in a SecurityException. You can manually assign permissions to each plugin by defining them in a permissions.properties file. The syntax is like the following:
`permissions.pluginFileName.files=read /path/to/file`

This snippet grants reading permissions on a file to the specified Plugin. The Plugin name is the name of the JAR file minus the extension, so for example if the file is called *plugin-1.0-RELEASE.jar* you will specify the property like:
`permissions.plugin-1.0-RELEASE.files= read /path/to/file`

Multiple permissions of the same type can be specified in a single property:
`permissions.plugin-1.0-RELEASE.files= read /path/to/file1, write /path/to/file1, read /path/to/file2`

***CURRENTLY SUPPORTED PERMISSIONS***
* permissions.pluginName.files= 
    * read /path/to/file
    * write /path/to/file
    * execute /path/to/file
    * delete /path/to/file

### Plugin Extensions

One of the key elements of PlugFace is its reusability. It does not bind itself to a specific usecase, type of application or environment. However, each application has different needs, and therefore should have a more specific API that Plugins should implement in order to enhance functionality. But how could this be achieved without having to provide an SDK that Plugins should install and implement? Well, the solutions is called ** loosely coupled API with automatic method discovery **. 
Which is just a fancy way to say "Java reflection and annotations working together".

Let's see how this works.

Imagine we have an application that require its plugins to expose a method called *sayHello()*, that accepts a `String` for the name of the person you want to greet and returns `void`. Using PlugFace's `ExtensionMethod` annotation, we can write this method in our Plugin class and mark it so that the application can discover it.
So in our `Test` class we will add the method:

```java
package com.matteojoliveau.test;

import com.matteojoliveau.plugface.PlugfaceContext;
import com.matteojoliveau.plugface.Plugin;
import com.matteojoliveau.plugface.PluginConfiguration;
import com.matteojoliveau.plugface.annotations.ExtensionMethod;

public class Test implements Plugin{
    //something
    
    @ExtensionMethod
    public String sayHello(String name) {
        return "Hello " + name + "!";
    }

}
```

Now this method will be automagically discovered by our `PluginManager` at load time and registered into the manager. Now it is possible to invoke the method through the `PluginManager` by calling `execExtension()` like so:

```java
    //Somewhere in the application
    
    String greeting = (String) manager.execExtension("testPlugin", "sayHello", "Matteo");
```
The method `execExtension()` accepts three parameters: 
* the name of the plugin that you want to call the method from
* the name of the method that you want to call
* a list of arguments for the method passed as varargs. If the method takes no argument, just pass `null`;

### License
Copyright 2017 Matteo Joliveau

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
