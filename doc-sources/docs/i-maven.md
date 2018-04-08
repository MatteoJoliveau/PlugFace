---
id: install-maven
title: Maven Installation
sidebar_label: Maven
---

PlugFace is accessible from [Maven Central](http://mvnrepository.com/artifact/org.plugface).

To download the latest stable release in Maven, add the following snippet to the <dependencies> section of your `pom.xml`:
```xml
<dependency>
    <groupId>org.plugface</groupId>
    <artifactId>plugface-core</artifactId>
    <version>${release.version}</version>
</dependency>
```

or, if using Spring
```xml
<dependency>
    <groupId>org.plugface</groupId>
    <artifactId>plugface-spring</artifactId>
    <version>${release.version}</version>
</dependency>
```

