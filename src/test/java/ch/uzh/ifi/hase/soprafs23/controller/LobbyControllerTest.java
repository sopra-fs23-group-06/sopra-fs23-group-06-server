package ch.uzh.ifi.hase.soprafs23.controller;

import ch.uzh.ifi.hase.soprafs23.constant.UserStatus;
import ch.uzh.ifi.hase.soprafs23.controller.UserController;
import ch.uzh.ifi.hase.soprafs23.entity.Lobby;
import ch.uzh.ifi.hase.soprafs23.entity.User;
import ch.uzh.ifi.hase.soprafs23.rest.dto.LobbyPostDTO;
import ch.uzh.ifi.hase.soprafs23.rest.dto.UserPostDTO;
import ch.uzh.ifi.hase.soprafs23.service.LobbyService;
import ch.uzh.ifi.hase.soprafs23.service.UserService;
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
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(LobbyController.class)
public class LobbyControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private LobbyService lobbyService;

    @Mock
    private SseEmitter sseEmitterMock;

    @Test
    public void addUserToLobby_validInput_userAddedToLobby() throws Exception {
        // given
        User user = new User();
        user.setLobby(123456L);
        user.setUsername("username");
        user.setId(1L);

        UserPostDTO userPostDTO = new UserPostDTO();
        userPostDTO.setLobby(123456L);
        userPostDTO.setUsername("username");
        userPostDTO.setId(1L);

        given(lobbyService.addToLobby(Mockito.any())).willReturn(user);

        // when/then -> do the request + validate the result
        MockHttpServletRequestBuilder postRequest = post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(userPostDTO));

        // then
        mockMvc.perform(postRequest)
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(user.getId().intValue())))
                .andExpect(jsonPath("$.username", is(user.getUsername())))
                .andExpect(jsonPath("$.lobby", is(user.getLobby().intValue())));
    }

    @Test
    public void createLobby_validInput_LobbyCreated() throws Exception {
        // create user list
        User user = new User();
        user.setId(1L);
        user.setLobby(2L);
        user.setUsername("username");
        ArrayList<User> list = new ArrayList<User>();
        list.add(user);
        // create Lobby
        Lobby lobby = new Lobby();
        lobby.setId(2L);
        lobby.setLobbyCode(123456L);
        lobby.setPlayers(list);

        LobbyPostDTO lobbyPostDTO = new LobbyPostDTO();
        lobbyPostDTO.setId(2L);
        lobbyPostDTO.setLobbyCode(123456L);
        lobbyPostDTO.setPlayers(list);

        given(lobbyService.createLobby()).willReturn(lobby);

        // when/then -> do the request + validate the result
        MockHttpServletRequestBuilder postRequest = post("/lobbies")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(lobbyPostDTO));

        // then
        mockMvc.perform(postRequest)
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(lobby.getId().intValue())))
                .andExpect(jsonPath("$.lobbyCode", is(lobby.getLobbyCode().intValue())))
                .andExpect(jsonPath("$.players", hasSize(list.size())))
                .andExpect(jsonPath("$.players[*].id", contains(list.stream().map(player -> player.getId().intValue()).toArray())))
                .andExpect(jsonPath("$.players[*].lobby", contains(list.stream().map(player -> player.getLobby().intValue()).toArray())))
                .andExpect(jsonPath("$.players[*].username", contains(list.stream().map(player -> player.getUsername()).toArray())));
    }

    @Test
    public void getLobby_validCode_thenReturnLobby() throws Exception {
        // create user list
        User user = new User();
        user.setId(1L);
        user.setLobby(2L);
        user.setUsername("username");
        ArrayList<User> list = new ArrayList<User>();
        list.add(user);
        // create Lobby
        Lobby lobby = new Lobby();
        lobby.setId(2L);
        lobby.setLobbyCode(123456L);
        lobby.setPlayers(list);


        given(lobbyService.checkLobby(Mockito.any())).willReturn(lobby);

        // when/then -> do the request + validate the result
        MockHttpServletRequestBuilder getRequest = get("/lobbies/123456")
                .contentType(MediaType.APPLICATION_JSON);

        // then
        mockMvc.perform(getRequest)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(lobby.getId().intValue())))
                .andExpect(jsonPath("$.lobbyCode", is(lobby.getLobbyCode().intValue())))
                .andExpect(jsonPath("$.players", hasSize(list.size())))
                .andExpect(jsonPath("$.players[*].id", contains(list.stream().map(player -> player.getId().intValue()).toArray())))
                .andExpect(jsonPath("$.players[*].lobby", contains(list.stream().map(player -> player.getLobby().intValue()).toArray())))
                .andExpect(jsonPath("$.players[*].username", contains(list.stream().map(player -> player.getUsername()).toArray())));

    }
    @Test
    public void joinLobby_validCode_thenReturnLobby() throws Exception {
        // create user list
        User user = new User();
        user.setId(1L);
        user.setLobby(2L);
        user.setUsername("username");
        ArrayList<User> list = new ArrayList<User>();
        list.add(user);
        // create Lobby
        Lobby lobby = new Lobby();
        lobby.setId(2L);
        lobby.setLobbyCode(123456L);
        lobby.setPlayers(list);


        given(lobbyService.joinLobby(Mockito.any())).willReturn(lobby);

        // when/then -> do the request + validate the result
        MockHttpServletRequestBuilder putRequest = put("/lobbies/123456")
                .contentType(MediaType.APPLICATION_JSON);

        // then
        mockMvc.perform(putRequest)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(lobby.getId().intValue())))
                .andExpect(jsonPath("$.lobbyCode", is(lobby.getLobbyCode().intValue())))
                .andExpect(jsonPath("$.players", hasSize(list.size())))
                .andExpect(jsonPath("$.players[*].id", contains(list.stream().map(player -> player.getId().intValue()).toArray())))
                .andExpect(jsonPath("$.players[*].lobby", contains(list.stream().map(player -> player.getLobby().intValue()).toArray())))
                .andExpect(jsonPath("$.players[*].username", contains(list.stream().map(player -> player.getUsername()).toArray())));
    }

    @Test
    public void getUsers_validCode_thenReturnUsers() throws Exception {
        // create user list
        User user = new User();
        user.setId(1L);
        user.setLobby(2L);
        user.setUsername("username");

        List<User> allUsers = Collections.singletonList(user);

        given(lobbyService.getUsers(Mockito.any())).willReturn(allUsers);

        MockHttpServletRequestBuilder getRequest = get("/lobbies/123456/users")
                .contentType(MediaType.APPLICATION_JSON);

        // then
        mockMvc.perform(getRequest)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", is(user.getId().intValue())))
                .andExpect(jsonPath("$[0].lobby", is(user.getLobby().intValue())))
                .andExpect(jsonPath("$[0].username", is(user.getUsername())))
                .andExpect(jsonPath("$", hasSize(allUsers.size())));
    }

    @Test
    public void leaveLobby_validCode_returnOK() throws Exception {
        // given
        User user = new User();
        user.setLobby(123456L);
        user.setUsername("username");
        user.setId(1L);

        UserPostDTO userPostDTO = new UserPostDTO();
        userPostDTO.setLobby(123456L);
        userPostDTO.setUsername("username");
        userPostDTO.setId(1L);

        Mockito.doNothing().when(lobbyService).removeUser(Mockito.any());

        MockHttpServletRequestBuilder putRequest = put("/lobbies/123456/leaveHandler")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(userPostDTO));

        mockMvc.perform(putRequest).andExpect(status().isOk());
    }

    @Test
    public void kickUser_validCode_returnOK() throws Exception {
        // given
        User user = new User();
        user.setLobby(123456L);
        user.setUsername("username");
        user.setId(1L);

        UserPostDTO userPostDTO = new UserPostDTO();
        userPostDTO.setLobby(123456L);
        userPostDTO.setUsername("username");
        userPostDTO.setId(1L);

        Mockito.doNothing().when(lobbyService).removeUser(Mockito.any());

        MockHttpServletRequestBuilder putRequest = put("/lobbies/123456/kickHandler")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(userPostDTO));

        mockMvc.perform(putRequest).andExpect(status().isOk());
    }

    @Test
    public void closeLobby_validCode_returnOK() throws Exception {

        Mockito.doNothing().when(lobbyService).closeLobby(Mockito.any());

        MockHttpServletRequestBuilder putRequest = put("/lobbies/123456/closeHandler")
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(putRequest).andExpect(status().isOk());
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
