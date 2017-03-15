# PlugFace Framework
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)
[![Version](https://img.shields.io/badge/Snapshot-v0.4.0--SNAPSHOT-green.svg)](https://nexus.matteojoliveau.com/#browse/browse/components:maven-snapshots)
[![GitHub release](https://img.shields.io/badge/Release-v0.3.0-blue.svg)](https://github.com/MatteoJoliveau/PlugFace/releases/latest)
[![Build Status](https://travis-ci.org/MatteoJoliveau/PlugFace.svg?branch=master)](https://travis-ci.org/MatteoJoliveau/PlugFace)

**PLUGFACE IS STILL IN EARLY DEVELOPMENT. WAIT FOR THE 1.0.0-RELEASE FOR PRODUCTION USE** 

PlugFace is a simple, lightweight, high abstraction plugin framework for Java applications. It focuses on simplicity, easy and clean API and modularity. Visit the [Wiki](https://github.com/MatteoJoliveau/PlugFace/wiki) for more in-depth information.  

**Browse the Javadoc [here](http://plugface.matteojoliveau.com/javadoc)**  

## Core Concepts
* Simple `Plugin` interface that provides a unified API to start, stop and configure plugins
* `PluginManager` utility class to load, configure and register plugins. It should be the primary way for applications to manage the PlugFace echosystems.
* A `PlugfaceContext` that acts as a repository for registered plugins and managers. It holds the reference to all `PluginManager` instances and to the plugins that have been registered.

### Download
There are many ways to download PlugFace. The easiest one is to use a build management system like **Maven** or **Gradle**.  
Check out the **[Download](https://github.com/MatteoJoliveau/PlugFace/wiki/Download)** section of the Wiki for the full list.

To download the latest stable release in Maven, add the following snippet to the `<dependencies>` section of your pom.xml:
```xml
<dependency>
    <groupId>org.plugface</groupId>
    <artifactId>plugface-core</artifactId>
    <version>${release.version}</version>
</dependency>
```

For Gradle, add the following lines to `build.gradle`:
```gradle
repositories {
    mavenCentral()
}
```
Then add:
```gradle
dependencies {
    compile 'org.plugface:plugface-core:0.4.0-SNAPSHOT$RELEASEVERSION'
}
```

### Example usage
It's really easy to start using plugins in your application. You will just need a `PlugfaceContext`, a `PluginManager` and a folder that contains the plugins, packaged in Jar format.

```java
//Somewhere in the application

PlugfaceContext context = new DefaultPlugfaceContext();

PluginManager manager = new DefaultPluginManager(context);

manager.setPluginFolder("/path/to/plugins");

manager.setPermissionsFile("/path/to/permissions.properties"); //optional if your plugins require special permissions

List<Plugin> loaded = manager.loadPlugins(true); //true sets the autoregister flag, automatically inserting the 
                                                //plugins in the context
manager.startAll(); 
```
That's all, PlugFace is working!

Check out the [Quickstart](https://github.com/MatteoJoliveau/PlugFace/wiki/Getting-Started) and [How It Works](https://github.com/MatteoJoliveau/PlugFace/wiki/How-It-Works) guides for more detailed instructions.

### License
*The MIT License*  
**Copyright 2017 Matteo Joliveau**

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
