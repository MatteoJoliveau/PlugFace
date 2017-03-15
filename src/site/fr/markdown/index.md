#BIENVENUE SUR PLUGFACE
PlugFace est le framework de prochaine génération pour les applications modulaires Java, capable de transformer un logiciel maladroit en une application agile et échangeable à chaud. Il dispose d'un noyau léger et agnostique API, découverte automatique des plugins, gestion de contexte centralisée et simple extension de l'API grâce à des annotations.

##Concepts de base
* Simple interface `Plugin` qui fournit une API unifiée pour démarrer, arrêter et configurer des plugins
* Classe d'utilitaires `PluginManager` pour charger, configurer et enregistrer des plugins. Il devrait être la principale façon pour les applications de gérer l'écosystème PlugFace.
* Un `PlugfaceContext` qui agit comme un référentiel pour les plugins et les managers enregistrés. Il contient une référence à toutes les instances `PluginManager` et aux plugins qui ont été enregistrés.
* Un sandbox qui renferme des plugins dans une zone sécurisée sans autorisation d'accès au monde réel.
* Un système d'autorisation pour accorder des fonctionnalités supplémentaires aux plugins, tels que la lecture et l'écriture de fichiers ou l'accès au réseau.
* Un mécanisme d'extension pour étendre l'API pour chaque application, sans avoir besoin de décharger un SDK ou des bibliothèques supplémentaires.