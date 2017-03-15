# WELCOME TO PLUGFACE

PlugFace is the next generation framework for Java modular applications, capable of turning a clunky software into an agile, hot swappable app. It features a lightweight core and agnostic API, plugin autodiscovery, centralized context management and simple API extension through annotations.

## Core Concepts
* Simple `Plugin` interface that provides a unified API to start, stop and configure plugins
* `PluginManager` utility class to load, configure and register plugins. It should be the primary way for applications to manage the PlugFace echosystems.
* A `PlugfaceContext` that acts as a repository for registered plugins and managers. It holds the reference to all `PluginManager` instances and to the plugins that have been registered.
* A sandbox that encloses plugins in a safe zone without permission to access the real world.
* A permission system to grant extra functionalities to plugins, such as reading and writing files or accessing the network.
* An expansion mechanism to extend the API in a per-application base, without the need to download additional SDKs or libraries.

