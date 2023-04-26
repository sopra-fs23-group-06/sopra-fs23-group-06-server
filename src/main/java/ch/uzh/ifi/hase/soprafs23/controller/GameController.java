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
        ArrayList<Card> playerHand = gameService.getPlayerHand(userId, lobbyCode);
        return playerHand;
    }

    @PutMapping("/games/{lobbyCode}/cardHandler")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ResponseBody
    public void playCard(@RequestParam(name = "userId") Long userId, @RequestBody Map<String, String> playedCard, @PathVariable Long lobbyCode) {
        String cardRank = playedCard.get("aRank");
        String cardColor = playedCard.get("color");
        gameService.playCard(userId, lobbyCode, cardRank, cardColor);

    }


}
