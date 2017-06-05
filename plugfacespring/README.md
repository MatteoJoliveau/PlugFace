# PlugFace Spring Integration
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)
[![Maven Central](https://img.shields.io/maven-central/v/org.plugface/plugface.svg)](https://search.maven.org/#search|ga|1|plugface-spring)

**PLUGFACE IS STILL IN EARLY DEVELOPMENT. WAIT FOR THE 1.0.0-RELEASE FOR PRODUCTION USE** 

PlugFace Spring is an integration between PlugFace Framework and Spring Framework.    
Using this module not only enables all the basic functionalities of PlugFace, but also allows to manage your 
PlugFace's classes as Spring Beans.

### PluginContext and PluginManager
In PlugFace Spring, there is a new `PluginContext` implementation called `SpringPluginContext`.   
This class is an extension of the `DefaultPluginContext` that also implements Spring's `ApplicationContextAware` interface
to be able to interact with the Spring Context. Also, some of the `PluginContext` methods have been overridden 
to check for plugins and plugin managers in both the PlugFace `PluginContext`  and the Spring `ApplicationContext`.   
This way it is possible to register Plugins and Managers as Spring Beans and have them managed by the Spring IoC container.

Of course this come with some limitations. Being the Spring `ApplicationContext` not dynamic, it is impossible
to load Plugins as Spring Beans at runtime. All the plugins coming from outside the application source code
(see, plugin jars) will be registered as normal PlugFace plugins.  

#### Spring Boot Integration
To simplify integration with Spring Boot and reduce the amount of code to write, PlugFace Spring supports autoconfiguration,
automatically creating a `SpringPluginContext` and a related `DefaultPluginManager` (with name *springDefaultPluginManager*) as Spring Beans at startup. 

If autoconfiguration is used, retrieving the PluginContext and default Manager is as simple as injecting them
as dependencies. 

```java

@Component
public class ExampleSpringComponent {
    private final PluginContext pluginContext;
    private final PluginManager pluginManager;
    
    @Autowired
    public ExampleSpringComponent(PluginContext pc, PluginManager pm) {
        this.pluginContext = pc; //SpringPluginContext
        this.pluginManager = pm; //DefaultPluginContext with name "springDefaultPluginManager"
    }
}
```