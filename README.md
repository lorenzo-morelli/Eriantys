
# Eriantys - A board game
<p align="center">
    <img src="https://www.craniocreations.it/wp-content/uploads/2021/06/Eriantys_scatola3Dombra-600x600.png">


## Introduction
This is our Software Engineering final project for the bachelor's graduation in Computer Engineering.
It consists in an implementation of a digital transposition of a physical existing game called [Eriantys](https://www.craniocreations.it/prodotto/eriantys/), using a client/server architecture and the Model-View-Controller design patter.
The server runs on a Command Line Interface (CLI).
Otherwise, the client is available with CLI or Graphical User Interface (GUI).

## Developed features
- Complete game rules, with both beginner and expert game mode.
- Socket TCP transmission between the client and the server,
- CLI and GUI for the client.
- Matches with 2, 3 or 4 players.
- Resilience to disconnections: it means that when a player is disconnected, if there are enough players connected, the game will go on anyways.
- Bot simulating a players' moves in case they're disconnected.

## Documentation
You can find all the documentation about the UMLs, the network protocol and the BPMN diagram in the "deliverables" folder.

### Testing
For the Model, ome tests have been done using Junit, with a coverage of 86% of the methods and 82% of the code lines.

![](https://i.ibb.co/M7dvkmN/Senza-titolo.png)


### Dependencies and plugins
We have chosen to use only the essential number of dependencies.
-	Gson
-	Junit
-	Javafx
-	Maven

## Developers
- [Fernando Morea](https://github.com/fmorea)
- [Lorenzo Morelli](https://github.com/lorenzo-morelli)
- [Ignazio Neto Dell'Acqua](https://github.com/Ighi01)
