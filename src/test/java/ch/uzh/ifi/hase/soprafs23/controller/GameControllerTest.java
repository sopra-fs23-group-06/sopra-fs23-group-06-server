package ch.uzh.ifi.hase.soprafs23.controller;

import ch.uzh.ifi.hase.soprafs23.Points.Score;
import ch.uzh.ifi.hase.soprafs23.Points.Scoreboard;
import ch.uzh.ifi.hase.soprafs23.entity.Card;
import ch.uzh.ifi.hase.soprafs23.entity.Deck;
import ch.uzh.ifi.hase.soprafs23.entity.Lobby;
import ch.uzh.ifi.hase.soprafs23.entity.Player;
import ch.uzh.ifi.hase.soprafs23.rest.dto.LobbyPostDTO;
import ch.uzh.ifi.hase.soprafs23.rest.dto.PlayerPostDTO;
import ch.uzh.ifi.hase.soprafs23.service.GameService;
import ch.uzh.ifi.hase.soprafs23.service.LobbyService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.*;

import static org.hamcrest.Matchers.*;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(GameController.class)
public class GameControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private GameService gameService;

    @Test
    public void getCardHandler_validInput_cardHandlerReturned() throws Exception {
        // generate a deck
        Deck deck = new Deck();
        deck.fillDeck();
        Player player = new Player();
        player.setLobby(123456L);
        player.setUsername("username");
        player.setId(1L);
        ArrayList<Card> playerHand = new ArrayList<Card>();
        playerHand.add(deck.draw());
        player.setHand(playerHand);

        given(gameService.getPlayerHand(Mockito.any(), Mockito.any())).willReturn(playerHand);

        // when/then -> do the request + validate the result
        MockHttpServletRequestBuilder getRequest = get("/games/123456/cardHandler").param("userId", "1")
                .contentType(MediaType.APPLICATION_JSON);

        // then
        mockMvc.perform(getRequest)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(playerHand.size())))
                .andExpect(jsonPath("$[0].aRank", is(playerHand.get(0).getaRank().toString())))
                .andExpect(jsonPath("$[0].playable", is(playerHand.get(0).getPlayable())))
                .andExpect(jsonPath("$[0].color", is(playerHand.get(0).getColor().toString())));
    }
    @Test
    public void putCardHandler_validInput_returnNoContent() throws Exception {
        // generate a deck
        Deck deck = new Deck();
        deck.fillDeck();
        Player player = new Player();
        player.setLobby(123456L);
        player.setUsername("username");
        player.setId(1L);
        ArrayList<Card> playerHand = new ArrayList<Card>();
        playerHand.add(deck.draw());
        player.setHand(playerHand);

        // Create an ObjectMapper to convert the Map to JSON
        ObjectMapper objectMapper = new ObjectMapper();

        // Create a Map with the data you want to send in the request body
        Map<String, String> playedCard = new HashMap<>();
        playedCard.put("aRank", "ONE");
        playedCard.put("color", "RED");

        // Convert the Map to a JSON string
        String requestBody = objectMapper.writeValueAsString(playedCard);

        Mockito.doNothing().when(gameService).playCard(Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any());

        // when/then -> do the request + validate the result
        MockHttpServletRequestBuilder putRequest = put("/games/123456/cardHandler").param("userId", "1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody);

        // then
        mockMvc.perform(putRequest)
                .andExpect(status().isNoContent());
    }
    @Test
    public void postGame_validInput_gameStarted() throws Exception {

        Mockito.doNothing().when(gameService).startGame(Mockito.any());

        // when/then -> do the request + validate the result
        MockHttpServletRequestBuilder postRequest = post("/games/123456")
                .contentType(MediaType.APPLICATION_JSON);

        // then
        mockMvc.perform(postRequest)
                .andExpect(status().isOk());
    }
    @Test
    public void getRounds_validInput_returnRounds() throws Exception {
        //given
        given(gameService.getRound(Mockito.any())).willReturn(1);

        // when/then -> do the request + validate the result
        MockHttpServletRequestBuilder getRequest = get("/games/123456/rounds")
                .contentType(MediaType.APPLICATION_JSON);

        // then
        mockMvc.perform(getRequest)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", is(1)));
    }
    @Test
    public void putBid_validInput_returnPlayerWithBid() throws Exception {
        // given
        Player player = new Player();
        player.setLobby(123456L);
        player.setUsername("username");
        player.setId(1L);
        player.setBid(0);
        player.setTricks(0);

        PlayerPostDTO playerPostDTO = new PlayerPostDTO();
        playerPostDTO.setLobby(123456L);
        playerPostDTO.setUsername("username");
        playerPostDTO.setId(1L);
        playerPostDTO.setBid(0L);
        playerPostDTO.setTricks(0);

        given(gameService.recordBid(Mockito.any(), Mockito.any())).willReturn(player);

        MockHttpServletRequestBuilder putRequest = put("/games/123456/bidHandler")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(playerPostDTO));

        mockMvc.perform(putRequest)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(player.getId().intValue())))
                .andExpect(jsonPath("$.lobby", is(player.getLobby().intValue())))
                .andExpect(jsonPath("$.bid", is(player.getBid())))
                .andExpect(jsonPath("$.tricks", is(player.getTricks())))
                .andExpect(jsonPath("$.username", is(player.getUsername())));

    }
    @Test
    public void getOrder_validInput_returnOrder() throws Exception {
        // create user list
        Player user = new Player();
        user.setId(1L);
        user.setLobby(123456L);
        user.setUsername("username");
        Player user2 = new Player();
        user2.setId(2L);
        user2.setLobby(123456L);
        user2.setUsername("username2");
        ArrayList<Player> list = new ArrayList<>();
        list.add(user);
        list.add(user2);
        // create Lobby
        Lobby lobby = new Lobby();
        lobby.setId(2L);
        lobby.setLobbyCode(123456L);
        lobby.setPlayers(list);

        given(gameService.getOrder(Mockito.any())).willReturn(list);

        MockHttpServletRequestBuilder getRequest = get("/games/123456/order")
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(getRequest)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(list.size())))
                .andExpect(jsonPath("$[0].username", is(list.get(0).getUsername())))
                .andExpect(jsonPath("$[0].id", is(list.get(0).getId().intValue())))
                .andExpect(jsonPath("$[0].lobby", is(list.get(0).getLobby().intValue())))
                .andExpect(jsonPath("$[1].username", is(list.get(1).getUsername())))
                .andExpect(jsonPath("$[1].id", is(list.get(1).getId().intValue())))
                .andExpect(jsonPath("$[1].lobby", is(list.get(1).getLobby().intValue())));
    }
    @Test
    public void getTableCards_validInput_returnTableCards() throws Exception {
        // generate a deck
        Deck deck = new Deck();
        deck.fillDeck();
        Player player = new Player();
        player.setLobby(123456L);
        player.setUsername("username");
        player.setId(1L);
        ArrayList<Card> tableCards = new ArrayList<Card>();
        tableCards.add(deck.draw());
        tableCards.add(deck.draw());

        given(gameService.getTableCards(Mockito.any())).willReturn(tableCards);

        MockHttpServletRequestBuilder getRequest = get("/games/123456/playedCards")
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(getRequest)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(tableCards.size())))
                .andExpect(jsonPath("$[0].aRank", is(tableCards.get(0).getaRank().toString())))
                .andExpect(jsonPath("$[0].playable", is(tableCards.get(0).getPlayable())))
                .andExpect(jsonPath("$[0].color", is(tableCards.get(0).getColor().toString())))
                .andExpect(jsonPath("$[1].aRank", is(tableCards.get(1).getaRank().toString())))
                .andExpect(jsonPath("$[1].playable", is(tableCards.get(1).getPlayable())))
                .andExpect(jsonPath("$[1].color", is(tableCards.get(1).getColor().toString())));
    }
    @Test
    public void getScoreboard_validInput_returnScoreboard() throws Exception {

        // create user list
        Player user = new Player();
        user.setId(1L);
        user.setLobby(123456L);
        user.setUsername("username");
        Player user2 = new Player();
        user2.setId(2L);
        user2.setLobby(123456L);
        user2.setUsername("username2");
        ArrayList<Player> list = new ArrayList<>();
        list.add(user);
        list.add(user2);

        // create scoreboard
        Scoreboard scoreboard = new Scoreboard(list);
        Score score = new Score();
        score.setCurPlayer(user);
        score.setCurRound(1);
        score.setCurBid(0);
        score.setCurPoints(0);
        scoreboard.setScoreboard(score);



        given(gameService.getScoreboard(Mockito.any())).willReturn(scoreboard);

        // when/then -> do the request + validate the result
        MockHttpServletRequestBuilder getRequest = get("/games/123456/scoreboard")
                .contentType(MediaType.APPLICATION_JSON);

        // then
        mockMvc.perform(getRequest)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.scoreboard", hasSize(2))) // verify scoreboard size
                .andExpect(jsonPath("$.scoreboard[0][0].curPlayer", is(user.getUsername()))) // verify curPlayer
                .andExpect(jsonPath("$.scoreboard[0][0].curRound", is(score.getCurRound()))) // verify curRound
                .andExpect(jsonPath("$.scoreboard[0][0].curBid", is(score.getCurBid()))) // verify curBid
                .andExpect(jsonPath("$.scoreboard[0][0].curPoints", is(score.getCurPoints()))); // verify curPoints
    }

    /**
     * Helper Method to convert userPostDTO into a JSON string such that the input
     * can be processed
     * Input will look like this: {"name": "Test User", "username": "testUsername"}
     *
     * @param object
     * @return string
     */
    private String asJsonString(final Object object) {
        try {
            return new ObjectMapper().writeValueAsString(object);
        } catch (JsonProcessingException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    String.format("The request body could not be created.%s", e.toString()));
        }
    }
}
