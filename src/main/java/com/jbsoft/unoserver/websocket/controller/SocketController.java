package com.jbsoft.unoserver.websocket.controller;

import com.jbsoft.unoserver.game.GameManager;
import com.jbsoft.unoserver.game.GamePlayerMove;
import com.jbsoft.unoserver.game.GameState;
import com.jbsoft.unoserver.game.PlayerConfig;
import com.jbsoft.unoserver.websocket.model.GameData;
import com.jbsoft.unoserver.websocket.model.UserData;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Controller;

import static java.lang.String.format;

@Controller
public class SocketController {

    private final SimpMessageSendingOperations messagingTemplate;

    public SocketController(SimpMessageSendingOperations messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    @MessageMapping("/{roomId}/addUser")
    public void greeting(@DestinationVariable String roomId, @Payload UserData userData)  {
        sendConfig(GameManager.the().getPlayerConfig(roomId, userData.getSessionId()));
        sendState(GameManager.the().getState(roomId));
    }

    @MessageMapping("/{roomId}/sendGameData")
    public void receiveGameData(@DestinationVariable String roomId, @Payload GameData data) {
        GamePlayerMove move = GameManager.the().updateState(roomId, data);
        sendMove(move, GameManager.the().getState(roomId));
    }

    private void sendMove(GamePlayerMove move, GameState state) {
        messagingTemplate.convertAndSend(format("/channel/%s", state.getRoomId()), move);
    }

    private void sendState(GameState state) {
        messagingTemplate.convertAndSend(format("/channel/%s", state.getRoomId()), state);
    }

    private void sendConfig(PlayerConfig config) {
        messagingTemplate.convertAndSend(format("/channel/%s/%s", config.getRoomId(), config.getSessionId()), config);
        System.out.printf("/channel/%s%n", config.getRoomId());
    }

}
