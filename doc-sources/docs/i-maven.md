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

Snapshots are accessible via Sonatype Nexus.  
Add this line to your `pom.xml` file:  
```xml
<repositories>
    <repository>
        <id>oss.sonatype.org-snapshot</id>
        <url>http://oss.sonatype.org/content/repositories/snapshots</url>
        <releases>
            <enabled>false</enabled>
        </releases>
        <snapshots>
            <enabled>true</enabled>
        </snapshots>
    </repository>
</repositories>
```

and then:  
```xml
<dependency>
    <groupId>org.plugface</groupId>
    <artifactId>plugface-core</artifactId>
    <version>${snapshot.version}-SNAPSHOT</version>
</dependency>
```

or, if using Spring
```xml
<dependency>
    <groupId>org.plugface</groupId>
    <artifactId>plugface-spring</artifactId>
    <version>${snapshot.version}-SNAPSHOT</version>
</dependency>
```

## JitPack
PlugFace is also accessible via [JitPack](https://jitpack.io/#matteojoliveau/PlugFace), follow the instruction at the provided link
on how to enable and get the latest version or the latest snapshot from the `develop` branch, which is the semi-stable WIP branch.

Notice that if using the JitPack version, the group-id is not the same as the one from Maven Central:

```xml
<dependency>
    <groupId>com.github.matteojoliveau.PlugFace</groupId>
    <artifactId>plugface-core</artifactId>
    <version>${release.version}</version>
</dependency>
```

```xml
<dependency>
    <groupId>com.github.matteojoliveau.PlugFace</groupId>
    <artifactId>plugface-core</artifactId>
    <version>develop-SNAPSHOT</version>
</dependency>
```