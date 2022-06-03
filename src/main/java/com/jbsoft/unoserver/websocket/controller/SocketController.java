package com.jbsoft.unoserver.websocket.controller;

import com.jbsoft.unoserver.Response;
import com.jbsoft.unoserver.game.model.Card;
import com.jbsoft.unoserver.game.model.ResponseImpl;
import com.jbsoft.unoserver.game.services.GameManager;
import com.jbsoft.unoserver.websocket.model.GameData;
import com.jbsoft.unoserver.websocket.model.UserData;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Controller;

import java.util.List;

import static java.lang.String.format;

@Controller
public class SocketController {

    private final SimpMessageSendingOperations messagingTemplate;

    public SocketController(SimpMessageSendingOperations messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    @MessageMapping("/{roomId}/addUser")
    public void greeting(@DestinationVariable String roomId, @Payload UserData userData) {
        ResponseImpl config = GameManager.the().getPlayerConfig(roomId, userData.getSessionId());
        sendPrivateResponse(config, config.getSessionId(), roomId);
        ResponseImpl state = GameManager.the().getState(roomId);
        state.getPlayers().forEach(p -> p.getHand().forEach(Card::hideData));
        sendPublicResponse(state, roomId);
    }

    @MessageMapping("/{roomId}/sendGameData")
    public void receiveGameData(@DestinationVariable String roomId, @Payload GameData data) {
        List<ResponseImpl> responses = GameManager.the().updateState(roomId, data);
        if (!responses.isEmpty()) {
            sendPrivateResponse(new ResponseImpl.ResponseBuilder().type(Response.Type.VALIDATE_PLAY).valid(true).build(), data.getSessionId(), roomId);
        } else {
            sendPrivateResponse(new ResponseImpl.ResponseBuilder().type(Response.Type.VALIDATE_PLAY).valid(false).build(), data.getSessionId(), roomId);
            return;
        }
        for (ResponseImpl r : responses) {
            if (r.getType().equals(Response.Type.DRAW)) {
                sendPrivateResponse(r, r.getSessionId(), roomId);
                r.hideCardData();
            }
            sendPublicResponse(r, roomId);
        }
    }

    @MessageMapping("/{roomId}/startGame")
    public void startGame(@DestinationVariable String roomId) {
        List<ResponseImpl> response = GameManager.the().startGame(roomId);
        response.forEach(r -> sendPrivateResponse(r, r.getSessionId(), roomId));
        response.forEach(ResponseImpl::hideCardData);
        response.forEach(r -> sendPublicResponse(r, roomId));
    }


    private void sendPublicResponse(ResponseImpl response, String roomId) {
        messagingTemplate.convertAndSend(format("/channel/%s", roomId), response);
        try {
            Thread.sleep(5);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void sendPrivateResponse(ResponseImpl response, String sessionId, String roomId) {
        messagingTemplate.convertAndSend(format("/channel/%s/%s", roomId, sessionId), response);
        try {
            Thread.sleep(5);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
