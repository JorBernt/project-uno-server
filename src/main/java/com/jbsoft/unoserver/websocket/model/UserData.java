package com.jbsoft.unoserver.websocket.model;

public class UserData {
    private String username, roomId, sessionId;

    public UserData(String username, String roomId, String sessionId) {
        this.username = username;
        this.roomId = roomId;
        this.sessionId = sessionId;
    }

    public UserData() { }

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
}
