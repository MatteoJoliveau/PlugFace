---
id: install-gradle
title: Gradle Installation
sidebar_label: Gradle
---

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
