# PlugFace Framework
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)
[![Maven Central](https://img.shields.io/maven-central/v/org.plugface/plugface.svg)](https://search.maven.org/#search|ga|1|plugface-core)
[![Build Status](https://travis-ci.org/MatteoJoliveau/PlugFace.svg?branch=master)](https://travis-ci.org/MatteoJoliveau/PlugFace)

**PLUGFACE IS STILL IN EARLY DEVELOPMENT. WAIT FOR THE 1.0.0-RELEASE FOR PRODUCTION USE**   
*This README refers to versions 0.6.0+ which has undergone a major rewrite*

PlugFace is a simple, lightweight, high abstraction plugin framework for Java applications.
It focuses on simplicity, easy and clean API and modularity. Visit the [Documentation](https://plugface.matteojoliveau.com) for more in-depth information.  

**Browse the Javadoc [here](https://plugface.matteojoliveau.com/apidocs/plugface-core/index.html)**  

## Core Concepts
* Simple `Plugin` annotation to mark entry points to be loaded and registered in the classpath at runtime.
* `PluginManager` utility class to load, configure and register plugins. It should be the primary way for applications to manage the PlugFace echosystems.
* A `PluginContext` that acts as a repository for registered plugins and managers. It holds the reference to all the plugins that have been registered.
* Pluggable source mechanism. Implementing the `PluginSource` interface allows for nearly infinite possibilities as to from where plugins are loaded.
* **Dependency injection**: plugins can require other plugins using the standard Java `@Inject` annotation. If using the `plugface-spring` module for
Spring integration, plugins can also have Spring bean injected in their constructors (but not the other way around). (See [Dependency Injection]())

### Download
There are many ways to download PlugFace. The easiest one is to use a build management system like **Maven** or **Gradle**.  
Check out the **[Download](https://plugface.matteojoliveau.com/docs/install-maven.html)** section of the Docs for the full list.  
*If using Spring Framework you may want to use `plugface-spring` instead of `plugface-core` for an out-of-the-box integration*

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
    compile 'org.plugface:plugface-core:$RELEASEVERSION'
}
```

### Quick Start
Check out the [Quickstart](https://plugface.matteojoliveau.com/docs/app-quickstart.html) guides for more detailed instructions.

### License
*The MIT License*  
**Copyright 2017 Matteo Joliveau**

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
