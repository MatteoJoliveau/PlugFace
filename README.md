# PlugFace Framework
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)
[![Version](https://img.shields.io/badge/Snapshot-v0.3.0--SNAPSHOT-green.svg)](https://nexus.matteojoliveau.com/#browse/browse/components:maven-snapshots)
[![GitHub release](https://img.shields.io/badge/Release-v0.2.0-blue.svg)](https://github.com/MatteoJoliveau/PlugFace/releases/latest)
[![SonarQube Coverage](https://img.shields.io/sonar/http/sonar.qatools.ru/ru.yandex.qatools.allure:allure-core/coverage.svg)](https://sonarqube.vault101.ga/dashboard/index?id=com.matteojoliveau.plugface%3Aplugface)

**PLUGFACE IS STILL IN EARLY DEVELOPMENT. WAIT FOR THE 1.0.0-RELEASE FOR PRODUCTION USE** 

PlugFace is a simple, lightweight, high abstraction plugin framework for Java applications. It focuses on simplicity, easy and clean API and modularity. Visit the [Wiki](https://github.com/MatteoJoliveau/PlugFace/wiki) for more in-depth information.

## Core Concepts
* Simple `Plugin` interface that provides a unified API to start, stop and configure plugins
* `PluginManager` utility class to load, configure and register plugins. It should be the primary way for applications to manage the PlugFace echosystems.
* A `PlugfaceContext` that acts as a repository for registered plugins and managers. It holds the reference to all `PluginManager` instances and to the plugins that have been registered.

### Download
There are many ways to download PlugFace. The easiest one is to use a build management system like **Maven** or **Gradle**.  
Check out the **[Download](https://github.com/MatteoJoliveau/PlugFace/wiki/Download)** section of the Wiki for the full list.

To download the latest stable release in Maven, add the following snippet to the `repositories` section of your `pom.xml` file:

```xml
<repository>
    <id>plugface-releases</id>
    <name>PlugFace Release Repository</name>
    <url>https://nexus.matteojoliveau.com/repository/maven-releases/</url>
</repository>
```

Then add your dependency as usual:
```xml
<dependency>
    <groupId>com.matteojoliveau.plugface</groupId>
    <artifactId>plugface-core</artifactId>
    <version>${release.version}</version>
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
    compile 'com.matteojoliveau.plugface:plugface-core:$RELEASEVERSION'
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

Check out the [Quickstart](https://github.com/MatteoJoliveau/PlugFace/wiki/Getting-Started) and [How It Works](https://github.com/MatteoJoliveau/PlugFace/wiki/How-It-Works) guides for more detailed instructions.

### License
Copyright 2017 Matteo Joliveau

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
