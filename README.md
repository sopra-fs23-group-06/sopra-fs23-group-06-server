# SoPra Group 6 - Skull King Online
The goal of this project is to create an online version of the popular game [Skull King](https://www.grandpabecksgames.com/products/skull-king). Skull King
Online allows up to 6 players to play this exciting pirates' contest in which you have to win tricks in order to get or lose glory and fame.
Whether you're a seasoned Skull King player or new to the game, our online version provides an immersive experience that will keep you coming back for more.

## Table of Content

- [Technologies used](#technologies-used)
- [High-level components](#high-level-components)
- [Deployment](#deployment)
- [Illustrations](#illustrations)
- [Roadmap](#roadmap)
- [Authors and acknowledgment](#authors-and-acknowledgment)
- [License](#license)

## Technologies used
* [React.js](https://react.dev/) - Frontend JavaScript library
* [Spring](https://spring.io/projects/spring-framework) - Framework that enables running JVM
* [Gradle](https://gradle.org/) - Build automation tool
* [Spring WebSocket](https://docs.spring.io/spring-framework/docs/current/reference/html/web.html#websocket) - Real-time interactive applications protocol
* [Jitsi Meet API](https://jitsi.github.io/handbook/docs/dev-guide/dev-guide-react-sdk) - API for the voice chat functionality

## High-level components

#### Player
The [Player](https://github.com/sopra-fs23-group-06/sopra-fs23-group-06-server/blob/main/src/main/java/ch/uzh/ifi/hase/soprafs23/entity/Player.java) JPA entity stores all the important information about a player like their ID, the lobby ID, the username and the cards on the player's hand.

#### LobbyController
The [LobbyController](https://github.com/sopra-fs23-group-06/sopra-fs23-group-06-server/blob/main/src/main/java/ch/uzh/ifi/hase/soprafs23/controller/LobbyController.java) is responsible for processing the RESTful GET, PUT and POST requests from the client related to the functionality of the lobby, and calling the right function in the [LobbyService](https://github.com/sopra-fs23-group-06/sopra-fs23-group-06-server/blob/main/src/main/java/ch/uzh/ifi/hase/soprafs23/service/LobbyService.java)

#### GameController
The [GameController](https://github.com/sopra-fs23-group-06/sopra-fs23-group-06-server/blob/main/src/main/java/ch/uzh/ifi/hase/soprafs23/controller/GameController.java) is responsible for processing the RESTful GET, PUT and POST requests from the client related to the functionality of the game, and calling the right function in the [GameService](https://github.com/sopra-fs23-group-06/sopra-fs23-group-06-server/blob/main/src/main/java/ch/uzh/ifi/hase/soprafs23/service/GameService.java)

#### GameService
The [GameService](https://github.com/sopra-fs23-group-06/sopra-fs23-group-06-server/blob/main/src/main/java/ch/uzh/ifi/hase/soprafs23/service/GameService.java) contains the functions called by the [GameController](https://github.com/sopra-fs23-group-06/sopra-fs23-group-06-server/blob/main/src/main/java/ch/uzh/ifi/hase/soprafs23/controller/GameController.java). It is responsible for recording bids, fetching the cards on a [Player](https://github.com/sopra-fs23-group-06/sopra-fs23-group-06-server/blob/main/src/main/java/ch/uzh/ifi/hase/soprafs23/entity/Player.java)'s hand and for updating the player state after a card has been played.

## Deployment

#### Clone Repository
Clone the server repository onto your local machine with the help of [Git](https://git-scm.com/downloads).
```bash 
git clone https://github.com/sopra-fs23-group-06/sopra-fs23-group-06-server.git
```

#### Build

```bash
./gradlew build
```

#### Run

```bash
./gradlew bootRun
```

You can verify that the server is running by visiting `localhost:8080` in your browser.

#### Test

```bash
./gradlew test
```

### Development Mode
You can start the backend in development mode, this will automatically trigger a new build and reload the application
once the content of a file has been changed.

Start two terminal windows and run:

`./gradlew build --continuous`

and in the other one:

`./gradlew bootRun`

If you want to avoid running all tests with every change, use the following command instead:

`./gradlew build --continuous -xtest`

## Illustrations
For illustrations of the UI of this application, please have a look at our [client](https://github.com/sopra-fs23-group-06/sopra-fs23-group-06-client) repository.


## Roadmap
Developers who want to contribute to our project could add the following features:
- Add some new and exciting game modes, such as playing with a reduced deck of cards.
- Extend the functionality of the voice chat API to highlight the player who is talking at any moment during the game.
- Add a persistent high score view so that one could see the all-time highest scores achieved in the game.

## Authors and acknowledgment
### Authors
* **Roman Mathis** - [Terebos](https://github.com/Terebos)
* **Nikolin Lotter** - [CodeHub3](https://github.com/CodeHub3)
* **Lorenzo Ladner** - [lorezh](https://github.com/lorezh)
* **Matej Gurica** - [bzns](https://github.com/bzns)
* **Linard Jaeggi** - [exostatistic](https://github.com/exostatistic)

### Acknowledgment
* This project is based on the [SoPra FS23 - Server Template](https://github.com/HASEL-UZH/sopra-fs23-template-server)
* We would like to thank our tutor [Luis](https://github.com/luis-tm) as well as the entire team involved in the Software Engineering Lab course at the University of Zurich.

## License
This project is licensed under the GNU AGPLv3 License - see the [LICENSE](https://github.com/sopra-fs23-group-06/sopra-fs23-group-06-server/blob/main/LICENSE) file for details.
