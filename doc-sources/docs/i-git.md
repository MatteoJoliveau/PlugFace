---
id: install-git
title: Git Installation
sidebar_label: Git
---

PlugFace can also be obtained by compiling the project manually. 
Simply clone the repository in a local directory:
```
git clone https://github.com/MatteoJoliveau/PlugFace.git
```

PlugFace uses the Maven build management tool, compiling and installing is done by launching the command `mvn clean install` in a terminal.   
This will compile `plugface-core`, `plugface-spring` and the parent POM, run the tests and install the framework in your local maven repository.
