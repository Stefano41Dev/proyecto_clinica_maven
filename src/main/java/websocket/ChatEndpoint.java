package websocket;

import jakarta.websocket.*;
import jakarta.websocket.server.ServerEndpoint;
import lombok.extern.slf4j.Slf4j;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@Slf4j
@ServerEndpoint("/chat")
public class ChatEndpoint {
    private static final Set<Session> sesionesActivas = Collections.synchronizedSet(new HashSet<>());

    @OnOpen
    public void onOpen(Session session) {
        sesionesActivas.add(session);
    }

    @OnMessage
    public void onMessage(String mensajeJson, Session session) {
        for (Session s : sesionesActivas) {
            if (s.isOpen()) {
                try {
                    s.getAsyncRemote().sendText(mensajeJson);
                } catch (Exception e) {
                    log.error("Error al enviar mensaje: {}", e.getMessage());
                }
            }
        }
    }

    @OnClose
    public void onClose(Session session) {
        sesionesActivas.remove(session);
    }

    @OnError
    public void onError(Session session, Throwable throwable) {
        log.error("Error en el websocket: {}", throwable.getMessage());
    }
}
