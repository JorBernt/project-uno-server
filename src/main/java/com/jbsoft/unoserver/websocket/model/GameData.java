package com.jbsoft.unoserver.websocket.model;

public class GameData {
    private String playerId;
    private String card;

    public GameData(String playerId, String card) {
        this.playerId = playerId;
        this.card = card;
    }

    public GameData () { }

    public String getPlayerId() {
        return playerId;
    }

    public void setPlayerId(String playerId) {
        this.playerId = playerId;
    }

    public String getCard() {
        return card;
    }

    public void setCard(String card) {
        this.card = card;
    }
}
