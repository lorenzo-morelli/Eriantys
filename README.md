# Prova finale di ingegneria del software - AA 2021-2022
![](https://www.craniocreations.it/wp-content/uploads/2021/06/Eriantys_scatola3Dombra-600x600.png)


Il progetto consiste nell'implementazione dell'edizione digitale del gioco da tavolo [Eriantys](https://www.craniocreations.it/prodotto/eriantys/), facendo uso di un'architettura    
client/server ed utilizzando il pattern Model-View-Controller.

Il client è stato implementato sia nella versione command line interface (CLI), che nella versione graphic user interface (GUI).
# Funzionalità sviluppate
- Regole del gioco complete (incluse tutte le carte personaggio)
- Socket TCP
- CLI e GUI
- Partite a 2, 3 e 4 giocatori
- Resilienza alle disconnessioni
# Documentazione

## UML

[UML iniziale](https://github.com/lorenzo-morelli/ing-sw-2022-morelli-morea-netodellacqua/blob/main/deliverables/Initial_UML_Eriantys_morelli_morea_netodellacqua.pdf)
TODO: inserire uml finale
## Testing e coverage
Sono stati effettuati testing di coverage con Junit con copertura pari all'86% dei metodi ed all'82% delle righe di codice del Model.  
![](https://i.ibb.co/M7dvkmN/Senza-titolo.png)

## Librerie e plugins
Si è scelto di utilizzare il minimo numero di librerie e dipendenze esterne.
-	Gson
-	Junit
-	javafx
-	maven