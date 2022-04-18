package com.jbsoft.unoserver.game;

import com.jbsoft.unoserver.Response;

public class PlayerConfig extends Response {
    private String username, roomId, sessionId;
    private int playerId;
    public PlayerConfig(Type type, String username, String roomId, String sessionID, int playerId) {
        super(type);
        this.username = username;
        this.roomId = roomId;
        this.sessionId = sessionID;
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
}
