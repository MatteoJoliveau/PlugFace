#BIENVENIDOS A PLUGFACE
PlugFace es el marco de generación siguiente para las aplicaciones Java modulares, capaz de convertir un software torpe en una aplicación ágil y intercambiable. Cuenta con un núcleo ligero y una API agnóstica, un descubrimirntos automatico de plugins, una gestión de contexto centralizada y una sencilla extensión de la API mediante anotaciones.

##Conceptos básicos
* Sencilla interfaz `Plugin` que proporciona una API unificada para iniciar, detener y configurar complementos
* Clase de utilidad `PluginManager` para cargar, configurar y registrar plugins. Debería ser la forma principal para que las aplicaciones administren los ecosistemas PlugFace.
* Un `PlugfaceContext` que actúa como un repositorio para plugins y gestores registrados. Contiene la referencia a todas las instancias de `PluginManager` y los plugins que se han registrado.
* Una sandbox que encierra plugins en una zona segura sin permiso para acceder al mundo real.
* Un sistema de permisos para conceder funcionalidades adicionales a los plugins, como leer y escribir archivos o acceder a la red.
* Un mecanismo de expansión para extender la API en una base por aplicación, sin necesidad de descargar SDKs o libraries.drary adicionales