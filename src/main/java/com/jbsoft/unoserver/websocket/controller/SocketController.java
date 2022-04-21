package com.jbsoft.unoserver.websocket.controller;

import com.jbsoft.unoserver.Response;
import com.jbsoft.unoserver.game.GameManager;
import com.jbsoft.unoserver.game.DataResponse;
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
    public void greeting(@DestinationVariable String roomId, @Payload UserData userData)  {
        DataResponse config = GameManager.the().getPlayerConfig(roomId, userData.getSessionId());
        sendPrivateResponse(config, config.getSessionId(), roomId);
        DataResponse state = GameManager.the().getState(roomId);
        sendPublicResponse(state, roomId);
    }

    @MessageMapping("/{roomId}/sendGameData")
    public void receiveGameDat(@DestinationVariable String roomId, @Payload GameData data) {
        List<DataResponse> moves = GameManager.the().updateState(roomId, data);
        for(DataResponse r : moves) {
            if(r.getType().equals(Response.Type.DRAW)) {
                sendPrivateResponse(r, r.getSessionId(), roomId);
                r.hideCardData();
            }
            sendPublicResponse(r, roomId);
        }
    }

    @MessageMapping("/{roomId}/startGame")
    public void receiveGameData(@DestinationVariable String roomId) {
        List<DataResponse> response = GameManager.the().startGame(roomId);
        response.forEach(r -> sendPrivateResponse(r, r.getSessionId(), roomId));
        response.forEach(DataResponse::hideCardData);
        response.forEach(r -> sendPublicResponse(r, roomId));
    }



    private void sendPublicResponse(DataResponse response, String roomId) {
        messagingTemplate.convertAndSend(format("/channel/%s", roomId), response);
        try {
            Thread.sleep(5);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void sendPrivateResponse(DataResponse response, String sessionId, String roomId) {
        messagingTemplate.convertAndSend(format("/channel/%s/%s", roomId, sessionId), response);
        try {
            Thread.sleep(5);
        } catch (Exception e) {

        }
    }



}
