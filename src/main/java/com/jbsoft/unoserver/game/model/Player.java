package com.jbsoft.unoserver.game.model;

import com.jbsoft.unoserver.Response;
import java.util.ArrayList;
import java.util.List;

public class Player {
    private String username, roomId, sessionId;
    private List<Card> hand;
    private int playerId;

    public Player(String username, String roomId, String sessionId, int playerId) {
        this.username = username;
        this.roomId = roomId;
        this.sessionId = sessionId;
        this.playerId = playerId;
        hand = new ArrayList<>();
    }

    public Player(String username, int playerId) {
        this.username = username;
        this.playerId = playerId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public int getPlayerId() {
        return playerId;
    }

    public void setPlayerId(int playerId) {
        this.playerId = playerId;
    }

    public List<Card> getHand() {
        return hand;
    }

    public void setHand(List<Card> hand) {
        this.hand = hand;
    }

    public void addCardsToHand(List<Card> cards) {
        hand.addAll(cards);
    }

    public void addCardToHand(Card card) {
        hand.add(card);
    }

    public ResponseImpl getConfig() {
        return new ResponseImpl.ResponseBuilder()
                .type(Response.Type.CONFIG)
                .username(username)
                .roomId(roomId)
                .sessionId(sessionId)
                .playerId(playerId)
                .build();
    }
}
