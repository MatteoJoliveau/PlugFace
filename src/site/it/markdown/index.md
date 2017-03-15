# BENVENUTI SU PLUGFACE
PlugFace è il framework di prossima generazione per applicazioni Java modulari, in grado di trasformare un software goffo in un agile, hot swappable app. È dotato di un core leggero e API agnostico, plug autodiscovery, gestione del contesto centralizzata e semplice estensione API attraverso le annotazioni.

## Concetti principali
* Semplice interfaccia `Plugin` che fornisce una API unificata per avviare, arrestare e configurare i plugin
* Classe di utility `PluginManager` per caricare, configurare e registrare i plugin. Dovrebbe essere il modo principale per le applicazioni per gestire l'ecosistema di PlugFace.
* Un `PlugfaceContext` che funge da repository per i plugin e i manager registrati. Contiene il riferimento a tutte le istanze `PluginManager` e dei plugin che sono stati registrati.
* Una sandbox che racchiude i plugin in una zona sicura, senza permessi di accesso al mondo reale.
* Un sistema di permessi per concedere funzionalità extra ai plugin, come la lettura e la scrittura di file o l'accesso alla rete.
* Un meccanismo di espansione per estendere l'API in base alla specifica applicazione, senza la necessità di scaricare SDK o librerie aggiuntive