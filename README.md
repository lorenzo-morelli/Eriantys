# Prova finale d'ingegneria del software - AA 2021-2022
<p align="center">
    <img src="https://www.craniocreations.it/wp-content/uploads/2021/06/Eriantys_scatola3Dombra-600x600.png">
p>


## Introduzione
Il progetto consiste nell'implementazione dell'edizione digitale del gioco da tavolo [Eriantys](https://www.craniocreations.it/prodotto/eriantys/), facendo uso di un'architettura client/server e utilizzando il pattern Model-View-Controller.

Il client è stato implementato sia nella versione command line interface (CLI), che nella versione graphic user interface (GUI).


## Componenti
- Fernando Morea
- Lorenzo Morelli
- Ignazio Neto Dell'Acqua

## Funzionalità sviluppate
- Regole del gioco complete (incluse tutte le carte personaggio)
- Socket TCP
- CLI e GUI
- Partite a due, tre e quattro giocatori
- Resilienza alle disconnessioni
- Bot che simula un giocatore (nel caso in cui un giocatore si è disconnesso)

## Come eseguire i JAR
- Per la GUI:
  - basta fare doppio click sul file .jar!
- Per la CLI:
  - Aprire il command prompt all'interno della cartella in cui si trova il file .jar.
  - Eseguire il seguente codice: `java -jar Eriantys-CLI-v1.0.0.jar`.
- Per il SERVER:
  - Aprire il command prompt all'interno della cartella in cui si trova il file .jar.
  - Eseguire il seguente codice: `java -jar Eriantys-SERVER-v1.0.0.jar`.

## Documentazione


### UML
[UML finale](https://github.com/lorenzo-morelli/ing-sw-2022-morelli-morea-netodellacqua/blob/main/deliverables/UML_final.pdf)
<br>
[UML iniziale](https://github.com/lorenzo-morelli/ing-sw-2022-morelli-morea-netodellacqua/blob/main/deliverables/UML_initial.pdf)


### Socket
[Sequence diagrams finali](https://github.com/lorenzo-morelli/ing-sw-2022-morelli-morea-netodellacqua/blob/main/deliverables/protocol_final.pdf)
<br>
[Documentazione iniziale](https://github.com/lorenzo-morelli/ing-sw-2022-morelli-morea-netodellacqua/blob/main/deliverables/protocol_initial.pdf)


### Controllore del client
[Documentazione IFML](https://github.com/lorenzo-morelli/ing-sw-2022-morelli-morea-netodellacqua/blob/main/deliverables/others/ClientController.jpg)


### Interazioni tra controllore del client e del server
[Documentazione BPMN](https://github.com/lorenzo-morelli/ing-sw-2022-morelli-morea-netodellacqua/blob/main/deliverables/others/principiant_game.bpmn)


### Peer review ricevute
[Protocol](https://github.com/lorenzo-morelli/ing-sw-2022-morelli-morea-netodellacqua/blob/main/deliverables/protocol_peer_review_ricevuta.pdf)
<br>
[UML](https://github.com/lorenzo-morelli/ing-sw-2022-morelli-morea-netodellacqua/blob/main/deliverables/UML_peer_review_ricevuta.pdf)


### Peer review fatte
[Protocol](https://github.com/lorenzo-morelli/ing-sw-2022-morelli-morea-netodellacqua/blob/main/deliverables/protocol_peer_review_fatta.pdf)
<br>
[UML](https://github.com/lorenzo-morelli/ing-sw-2022-morelli-morea-netodellacqua/blob/main/deliverables/protocol_peer_review_ricevuta.pdf)


### Testing e coverage
Sono stati effettuati testing di coverage con Junit con copertura pari all'86% dei metodi e all'82% delle righe di codice del Model.  

![](https://i.ibb.co/M7dvkmN/Senza-titolo.png)


### Librerie e plugins
Si è scelto di utilizzare il minimo numero di librerie e dipendenze esterne.
-	Gson
-	Junit
-	javafx
-	maven
