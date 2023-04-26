package ch.uzh.ifi.hase.soprafs23.service;

import ch.uzh.ifi.hase.soprafs23.Points.Trick;
import ch.uzh.ifi.hase.soprafs23.entity.Card;
import ch.uzh.ifi.hase.soprafs23.entity.Lobby;
import ch.uzh.ifi.hase.soprafs23.entity.Player;
import ch.uzh.ifi.hase.soprafs23.repository.LobbyRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
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
        if(lobby==null)
    {
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Lobby does not exist.");
    }

    ArrayList<Player> playerList = lobby.getPlayers();
    Player player = null;
    ArrayList<Card> playerHand = null;
        for(
    int i = 0; i<playerList.size();i++)

    {
        if (playerList.get(i).getId().equals(userId)) {
            player = playerList.get(i);
            playerHand = player.getHand();
        }
    }
        return playerHand;
}

    public void playCard(Long userId, Long lobbyCode, String cardRank, String cardColor) {
        Lobby lobby = lobbyRepository.findByLobbyCode(lobbyCode);
        if(lobby==null)
        {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Lobby does not exist.");
        }

        ArrayList<Player> playerList = lobby.getPlayers();
        Player player = null;
        ArrayList<Card> playerHand = null;
        for (int i = 0; i < playerList.size(); i++) {
            if (playerList.get(i).getId().equals(userId)) {
                player = playerList.get(i);
                playerHand = player.getHand();
            }
        }

        Card playedCard = null;
        for (int i = 0; i < playerHand.size(); i++) {
            Card card = playerHand.get(i);
            if (card.getaRank().toString().equals(cardRank) && card.getColor().toString().equals(cardColor)) {
                playedCard = card;
                playerHand.remove(i);
                break;
            }
        }

        if (playedCard == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Card not found in player's hand.");
        }

        Trick trick = lobby.getGameLogic().getTrick();
        if (trick==null) {trick = new Trick();} //JUST FOR TEST PURPOSE
        trick.addPlayedCards(playedCard);
    }

}
