#WILLKOMMEN ZU PLUGFACE

PlugFace ist die nächste Generation-Framework für Java modulare  Applikationen, es kann, eine schlechte
Software in einer agile, Hot-Swap-App verwandeln . Es ist ausgestattet mit leichtem Kern und Agnostiker 
API, automatische Erkennung Plugin, zentrale Kontextmanagement und einfache API-Erweiterung durch 
Anmerkungen.

## Kernkonzepte

* Einfache Interface Plugin, das eine einheitliche API bietet, um Plugins zu starten, stoppen und 
konfigurieren
* `PluginManager` Utility-Klasse, um Plugins zu laden, konfigurieren und registrieren. Es sollt der wichtigste 
Weg für Anwendungen sein, um die PlugFace Echosystem zu verwalten.
* PlugfaceContext`, das fungiert als Repository für Plugins und registrierte Manager. Es enthält den Hinweis 
auf alle `PluginManager` Instanzen und auf den Plugins, die registriert worden haben.
* Eine Sandbox, das Plugins in einem sicheren Bereich zusammenfasst, ohne zugriffsberechtigung auf die 
reale Welt. 
* Ein Berechtigung-System, um zusätzlichen Funktionalitäten zu Plugins erteilen, wie das Lesen und 
Schreiben von Dateien oder das Netzzugangs.
* Eine Erweiterungsmechanismus, um die API auf der Grundlage spezifischen Applikationen zu verbreiten, 
ohne die Notwendigkeit SDK  zu downloaden, oder zusätzliche Bibliotheken zu benutzen.