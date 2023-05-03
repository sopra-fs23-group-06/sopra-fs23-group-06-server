package ch.uzh.ifi.hase.soprafs23.service;

import ch.uzh.ifi.hase.soprafs23.Game.GameLogic;
import ch.uzh.ifi.hase.soprafs23.Points.Calculate;
import ch.uzh.ifi.hase.soprafs23.Points.Evaluate;
import ch.uzh.ifi.hase.soprafs23.Points.Trick;
import ch.uzh.ifi.hase.soprafs23.entity.Card;
import ch.uzh.ifi.hase.soprafs23.entity.Lobby;
import ch.uzh.ifi.hase.soprafs23.entity.Player;
import ch.uzh.ifi.hase.soprafs23.repository.LobbyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class GameService {
    private final LobbyRepository lobbyRepository;

    @Autowired
    public GameService(@Qualifier("lobbyRepository") LobbyRepository lobbyRepository) {
        this.lobbyRepository = lobbyRepository;
    }


    public ArrayList<Card> getPlayerHand(Long userId, Long lobbyCode) {
        Lobby lobby = lobbyRepository.findByLobbyCode(lobbyCode);
        if(lobby==null) {throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Lobby does not exist.");}
    ArrayList<Player> playerList = lobby.getPlayers();
    Player player = null;
    ArrayList<Card> playerHand = null;
        for (Player value : playerList) {
            if (value.getId().equals(userId)) {
                player = value;
                lobby.getGameLogic().checkHand();
                playerHand = player.getHand();
            }
        }
        return playerHand;
}

    public void playCard(Long userId, Long lobbyCode, String cardRank, String cardColor, String cardOption) {
        Lobby lobby = lobbyRepository.findByLobbyCode(lobbyCode);
        if(lobby==null) {throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Lobby does not exist.");}
        ArrayList<Player> playerList = lobby.getPlayers();
        Player player = null;
        ArrayList<Card> playerHand = null;
        for (Player value : playerList) {
            if (value.getId().equals(userId)) {
                player = value;
                playerHand = player.getHand();
            }
        }
        assert player != null;
        if (!player.isHasTurn()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "It is not your turn");
        }
        Card playedCard = null;
        for (int i = 0; i < playerHand.size(); i++) {
            Card card = playerHand.get(i);
            if (card.getaRank().toString().equals(cardRank) && card.getColor().toString().equals(cardColor)) {
                if(!cardOption.equals("NONE")){
                    card.setScaryMary(cardOption);
                }
                playedCard = card;
                playerHand.remove(i);
                player.setHasTurn(false);
                break;
            }
        }
        if (playedCard == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Card not found in player's hand.");
        }

        Trick trick = lobby.getGameLogic().getTrick();
        trick.addPlayedCards(playedCard);
        player.playCard(playedCard,trick);
        if (trick.getPlayedCards().size() == lobby.getPlayers().size()){
            lobby.getGameLogic().endTrick();
        }
        else {lobby.getPlayers().get((lobby.getPlayers().indexOf(player)+1) % lobby.getPlayers().size()).setHasTurn(true);}
    }


    public Player recordBid(Player playerInput, Long lobbyCode) {
        Lobby lobby = lobbyRepository.findByLobbyCode(lobbyCode);
        if(lobby==null) {throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Lobby does not exist.");}
        if (playerInput.getBid() < 0 || playerInput.getBid() > lobby.getRound()){
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Bid does not match round.");
        }
        ArrayList<Player> playerList = lobby.getPlayers();
        Player player = null;
        for (Player value : playerList) {
            if (value.getId().equals(playerInput.getId())) {
                player = value;
                player.setBid(playerInput.getBid());
            }
        }
        return player;
    }

    private boolean checkAllBidsMade(Lobby lobby) {
        boolean allBidsMade = true;
        ArrayList<Player> playerList = lobby.getPlayers();
        for (Player player : playerList) {
            if(player.getBid() == null){
                allBidsMade = false;
                break;
            }
        }
        return allBidsMade;
    }

    public void startGame(Long lobbyCode) {
        Lobby lobby = lobbyRepository.findByLobbyCode(lobbyCode);
        if(lobby==null) {throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Lobby does not exist.");}
        if(lobby.getPlayers().size() < 2 || lobby.getPlayers().size() >6)
        {throw new ResponseStatusException(HttpStatus.CONFLICT, "Incorrect Player count.");}
        for (int i = 0; i<lobby.getPlayers().size(); i++){
            lobby.getGameTable().addPlayer(lobby.getPlayers().get(i));
        }
        lobby.getGameTable().setRoundStarter(lobby.getPlayers().get(0));
        lobby.getGameLogic().nextRound();
    }


    public int getRound (Long lobbyCode){
        Lobby lobby = lobbyRepository.findByLobbyCode(lobbyCode);
        if(lobby==null) {throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Lobby does not exist.");}
        return lobby.getRound();
    }

    public List<Player> getOrder (Long lobbyCode){
        Lobby lobby = lobbyRepository.findByLobbyCode(lobbyCode);
        if(lobby==null) {throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Lobby does not exist.");}
        return lobby.getGameTable().getOrder();
    }

    public ArrayList<Card> getTableCards(Long lobbyCode) {
        Lobby lobby = lobbyRepository.findByLobbyCode(lobbyCode);
        if(lobby==null) {throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Lobby does not exist.");}
        return lobby.getGameLogic().getTrick().getPlayedCards();
    }
}
