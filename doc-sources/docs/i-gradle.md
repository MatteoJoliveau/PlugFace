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

Snapshots are accessible via [JitPack](https://jitpack.io/#matteojoliveau/PlugFace/develop-SNAPSHOT), follow the instruction at the provided link
on how to enable and get the latest snapshot version from the `develop` branch, which is the semi-stable WIP branch.

Notice that if using the JitPack version, the group-id is not the same as the one from Maven Central:
```groovy
dependencies {
    compile 'com.github.matteojoliveau.PlugFace:plugface-core:develop-SNAPSHOT'
}
```