package ch.uzh.ifi.hase.soprafs23.controller;

import ch.uzh.ifi.hase.soprafs23.entity.Card;
import ch.uzh.ifi.hase.soprafs23.entity.Player;
import ch.uzh.ifi.hase.soprafs23.points.Scoreboard;
import ch.uzh.ifi.hase.soprafs23.rest.dto.PlayerGetDTO;
import ch.uzh.ifi.hase.soprafs23.rest.dto.PlayerPostDTO;
import ch.uzh.ifi.hase.soprafs23.rest.mapper.DTOMapper;
import ch.uzh.ifi.hase.soprafs23.service.GameService;
import ch.uzh.ifi.hase.soprafs23.websockets.WebSocketHandler;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.socket.TextMessage;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@RestController
public class GameController {
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();
    private final GameService gameService;
    private final WebSocketHandler webSocketHandler;

    GameController(GameService gameService) {this.gameService = gameService;
        this.webSocketHandler = WebSocketHandler.getInstance();}

    @GetMapping("/games/{lobbyCode}/cardHandler")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public List<Card> getPlayerHand(@RequestParam(name = "userId") Long userId, @PathVariable Long lobbyCode) {
        return gameService.getPlayerHand(userId, lobbyCode);
    }

    @PutMapping("/games/{lobbyCode}/cardHandler")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ResponseBody
    public void playCard(@RequestParam(name = "userId") Long userId, @RequestBody Map<String, String> playedCard, @PathVariable Long lobbyCode) {
        String cardRank = playedCard.get("aRank");
        String cardColor = playedCard.get("color");
        String cardOption = playedCard.get("aOption");
        gameService.playCard(userId, lobbyCode, cardRank, cardColor, cardOption);
        TextMessage message = new TextMessage(lobbyCode +" update");
        try {
            webSocketHandler.sendServerMessage(message);
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
        CompletableFuture<Void> afterPlayCardFuture = CompletableFuture.runAsync(() -> {
            gameService.afterPlayCard(userId, lobbyCode);
        });

// Wait for the afterPlayCardFuture to complete before sending the server message again
        afterPlayCardFuture.thenRun(() -> {
            try {
                webSocketHandler.sendServerMessage(message);
            }
            catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }


    @PostMapping("/games/{lobbyCode}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public void startGame(@RequestBody PlayerPostDTO playerPostDTO, @PathVariable Long lobbyCode){
        // convert user
        Player playerInput = DTOMapper.INSTANCE.convertPlayerPostDTOtoEntity(playerPostDTO);
        if (playerInput.getId() != 1L) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Only the host can start the game");
        }
        gameService.startGame(lobbyCode);
        TextMessage message = new TextMessage(lobbyCode +" update");
        try {
            webSocketHandler.sendServerMessage(message);
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping("/games/{lobbyCode}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public void /*LobbyGetDTO*/ getGame(@PathVariable Long lobbyCode){

        //return
    }

    @GetMapping("/games/{lobbyCode}/rounds")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public int getRound(@PathVariable Long lobbyCode){
        return gameService.getRound(lobbyCode);
    }

    @PutMapping("/games/{lobbyCode}/bidHandler")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public PlayerGetDTO makeBid(@RequestBody PlayerPostDTO playerPostDTO, @PathVariable Long lobbyCode) {
        // convert user
        Player playerInput = DTOMapper.INSTANCE.convertPlayerPostDTOtoEntity(playerPostDTO);

        Player bidPlayer = gameService.recordBid(playerInput, lobbyCode);
        // convert internal representation of user back to API
        TextMessage message = new TextMessage(lobbyCode +" update");
        try {
            webSocketHandler.sendServerMessage(message);
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
        return DTOMapper.INSTANCE.convertEntityToPlayerGetDTO(bidPlayer);
    }

    @GetMapping("/games/{lobbyCode}/order")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public List<Player> getOrder(@PathVariable Long lobbyCode){
        return gameService.getOrder(lobbyCode);
    }

    @GetMapping("/games/{lobbyCode}/playedCards")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public List<Card> getTableCards(@PathVariable Long lobbyCode) {
        return gameService.getTableCards(lobbyCode);
    }

    @GetMapping("/games/{lobbyCode}/scoreboard")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public Scoreboard getScoreboard(@PathVariable Long lobbyCode){
        return gameService.getScoreboard(lobbyCode);
    }

    @GetMapping("/games/{lobbyCode}/trickWinner")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public Player getTrickWinner(@PathVariable Long lobbyCode){
        return gameService.getTrickWinner(lobbyCode);
    }
}
