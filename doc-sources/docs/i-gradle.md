---
id: install-gradle
title: Gradle Installation
sidebar_label: Gradle
---
## Stable

For Gradle, add the following lines to `build.gradle`. You can check the correct version on [Maven Central](http://mvnrepository.com/artifact/org.plugface):

```groovy
repositories {
    mavenCentral()
}
```
Then add:

```groovy
dependencies {
    compile 'org.plugface:plugface-core:$RELEASEVERSION'
}
```

## Snapshots
Snapshots are accessible via Sonatype Nexus.  
Add this line to your `build.gradle` file:

```groovy
repositories {
    maven { url "https://oss.sonatype.org/content/repositories/snapshots" }
}
```
Then add:

```groovy
dependencies {
    compile 'org.plugface:plugface-core:$SNAPSHOTVERSION'
}
```
  
## JitPack

PlugFace is also accessible via [JitPack](https://jitpack.io/#matteojoliveau/PlugFace), follow the instruction at the provided link
on how to enable and get the latest version or the latest snapshot from the `develop` branch, which is the semi-stable WIP branch.

Notice that if using the JitPack version, the group-id is not the same as the one from Maven Central:
```groovy
dependencies {
    compile 'com.github.matteojoliveau.PlugFace:plugface-core:develop-SNAPSHOT'
}
```