# Final project for Software Engineering course at Politecnico di Milano - AY 2021-2022

### Section of Professor San Pietro - Group PSP06

This repository contains the implementation of the multi-player board game Eriantys. The project follows the MVC pattern and enables the player to create
and run matches through the network. Maven was used to manage dependencies and to facilitate the development process as a whole.

<p align="center">
	<img src="https://raw.githubusercontent.com/antoorofino/ing-sw-2022-Moretti-Mormile-Orofino/main/eriantys.png" alt="ERIANTYS"/>
</p>

---

## Functionalities and features

- [x] 2-3 players match
- [x] Basic game mode
- [x] Expert game mode
- [x] Command line interface CLI
- [x] Graphical user interface GUI
- [x] Socket
- [x] AF - All character cards implemented
- [x] AF - Multiple matches at the same time
- [x] EXTRA - Drag and Drop feuture in GUI


## Libraries used

|Library/Framework|Description|
|-----------------|-----------|
|__maven__|used to facilitate the development process|
|__junit__|framework used for unit testing|
|__JavaFx__|java library used for the GUI|
|__maven-shade__|maven plugin used to generate the jars|
|__jansi__|maven plugin used to enable ANSI code on windows command prompt|

## Setup

Both the game server and the client interface are generated as jar files. They can be found in [deliverables/JAR](deliverables/JAR) folder.
- The server can be run with the following command, as default it runs on port 8090:
    ```shell
    > java -jar Eriantys_server.jar [-h] [-p port-number] [-v logger-verbose-level]
    ```
  The available arguments are:
    - **-h**: to get help;
    - **-p**: followed by the desired port number;
    - **-v**: followed by the desired level of logging;
    - **-log**: followed by a file name, to activate logging both in the console and in the chosen file.

- The client can be run with the following command, as default it will open the graphical interface:
    ```shell
    > java -jar Eriantys_client.jar [-h] [-cli]
    ```
  The available arguments are:
    - **-h**: to get help;
    - **-cli**: to tun the game on the cli.

## Testing and documentation

### Code coverage

The following figure shows the coverage of the unit tests.



### UML diagrams

- [UML/initial](deliverables/UML/initial) contains the initil UML diagram from an high level prospective;
- [UML/final](deliverables/UML/final) contains the final UML diagram autogenerated by IntelliJ.

### Communication protocol

[deliverables/communication_protocol](deliverables/communication_protocol) contains the report that explains how the communication is handled between server and client and a description of each message used in the different phases of the game.

### Peer review reports

The material sent and received for each peer-review can be found in [deliverables/peer_reviews](deliverables/peer_reviews).

---

### Credits
This project is developed in collaboration with [Politecnico di Milano](https://www.polimi.it) and [Cranio Creations](http://www.craniocreations.it)
who reserves the images copyright.

### Group components:
- Moretti Lorenzo ([@Lomoretti](https://github.com/Lomoretti))
- Mormile Matteo ([@teomormi](https://github.com/teomormi))
- Orofino Antonino ([@antoorofino](https://github.com/antoorofino))
