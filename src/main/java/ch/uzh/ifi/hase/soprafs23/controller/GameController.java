package ch.uzh.ifi.hase.soprafs23.controller;

import ch.uzh.ifi.hase.soprafs23.entity.Card;
import ch.uzh.ifi.hase.soprafs23.entity.Lobby;
import ch.uzh.ifi.hase.soprafs23.entity.Player;
import ch.uzh.ifi.hase.soprafs23.rest.dto.LobbyGetDTO;
import ch.uzh.ifi.hase.soprafs23.rest.dto.PlayerGetDTO;
import ch.uzh.ifi.hase.soprafs23.rest.dto.PlayerPostDTO;
import ch.uzh.ifi.hase.soprafs23.rest.mapper.DTOMapper;
import ch.uzh.ifi.hase.soprafs23.service.GameService;
import ch.uzh.ifi.hase.soprafs23.service.LobbyService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
public class GameController {

    private final GameService gameService;

    GameController(GameService gameService) {this.gameService = gameService;}

    @GetMapping("/games/{lobbyCode}/cardHandler")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public ArrayList<Card> getPlayerHand(@RequestParam(name = "userId") Long userId, @PathVariable Long lobbyCode) {
        return gameService.getPlayerHand(userId, lobbyCode);
    }

    @PutMapping("/games/{lobbyCode}/cardHandler")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ResponseBody
    public void playCard(@RequestParam(name = "userId") Long userId, @RequestBody Map<String, String> playedCard, @PathVariable Long lobbyCode) {
        String cardRank = playedCard.get("aRank");
        String cardColor = playedCard.get("color");
        gameService.playCard(userId, lobbyCode, cardRank, cardColor);

    }

    @PostMapping("/games/{lobbyCode}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public void startGame(@PathVariable Long lobbyCode){
        gameService.startGame(lobbyCode);
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
    public ArrayList<Card> getTableCards(@PathVariable Long lobbyCode) {
        return gameService.getTableCards(lobbyCode);
    }
}
