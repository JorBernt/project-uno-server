package com.jbsoft.unoserver.game;

import com.jbsoft.unoserver.Response;

public class Player {
    private String username, roomId, sessionId;
    private int playerId;

    public Player(String username, String roomId, String sessionId, int playerId) {
        this.username = username;
        this.roomId = roomId;
        this.sessionId = sessionId;
        this.playerId = playerId;
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

    public PlayerConfig getConfig() {
        return new PlayerConfig(Response.Type.CONFIG, username, roomId, sessionId, playerId);
    }
}
