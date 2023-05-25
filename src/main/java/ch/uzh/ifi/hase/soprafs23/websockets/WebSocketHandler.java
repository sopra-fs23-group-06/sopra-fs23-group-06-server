package ch.uzh.ifi.hase.soprafs23.websockets;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
public class WebSocketHandler extends TextWebSocketHandler {

    private static WebSocketHandler instance;

    private final List<WebSocketSession> webSocketSessions = new ArrayList<>();

    private WebSocketHandler() {
        // Private constructor to prevent direct instantiation
    }

    public static synchronized WebSocketHandler getInstance() {
        if (instance == null) {
            instance = new WebSocketHandler();
        }
        return instance;
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        webSocketSessions.add(session);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        webSocketSessions.remove(session);
    }

    public void sendServerMessage(TextMessage message) throws IOException {
        for (WebSocketSession webSocketSession : webSocketSessions) {
            webSocketSession.sendMessage(message);
        }
    }
}