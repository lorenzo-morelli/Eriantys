
# Eriantys - A board game
<p align="center">
    <img src="https://www.craniocreations.it/wp-content/uploads/2021/06/Eriantys_scatola3Dombra-600x600.png">


## Introduction
This project represents our culmination of the Software Engineering program as part of our pursuit of a bachelor's degree in Computer Engineering. Its objective is to develop a digital rendition of the physical game known as [Eriantys](https://craniointernational.com/products/eriantys/), employing a client/server architecture and adhering to the Model-View-Controller design pattern.
The server component operates within a Command Line Interface (CLI) environment, while the client offers the choice of either a CLI or a Graphical User Interface (GUI).

## Developed features
- Complete [game rules](https://craniointernational.com/2021/wp-content/uploads/2021/06/Eriantys_rules_small.pdf), with both beginner and expert game mode.
- Socket TCP transmission between the client and the server.
- CLI and GUI for the client.
- Matches with 2, 3 or 4 players.
- Resilience to disconnections: it means that when a player is disconnected, if there are enough players connected, the game will go on anyways.
- Bot simulating a player's moves in case they're disconnected.

## Documentation
You can find all the documentation about the UMLs, the network protocol and the BPMN diagram in the "deliverables" folder.

### Testing
For the Model, ome tests have been done using Junit, with a coverage of 86% of the methods and 82% of the code lines.

![](https://i.ibb.co/M7dvkmN/Senza-titolo.png)


### Dependencies and plugins
We have chosen to only use the essential number of dependencies.
-	Gson
-	Junit
-	Javafx
-	Maven

## Developers
- [Fernando Morea](https://github.com/fmorea)
- [Lorenzo Morelli](https://github.com/lorenzo-morelli)
- [Ignazio Neto Dell'Acqua](https://github.com/Ighi01)
