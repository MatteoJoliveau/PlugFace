---
id: install-maven
title: Maven Installation
sidebar_label: Maven
---

## Stable

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

## Snapshots

Snapshots are accessible via [JitPack](https://jitpack.io/#matteojoliveau/PlugFace/develop-SNAPSHOT), follow the instruction at the provided link
on how to enable and get the latest snapshot version from the `develop` branch, which is the semi-stable WIP branch.

Notice that if using the JitPack version, the group-id is not the same as the one from Maven Central:
```xml
<dependency>
    <groupId>com.github.matteojoliveau.PlugFace</groupId>
    <artifactId>plugface-core</artifactId>
    <version>develop-SNAPSHOT</version>
</dependency>
```