package com.jbsoft.unoserver.game;

import com.jbsoft.unoserver.Response;

public class GamePlayerMove extends Response {
    private String playerId;
    private String card;

    public GamePlayerMove(Type type, String playerId, String card) {
        super(type);
        this.playerId = playerId;
        this.card = card;
    }

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
