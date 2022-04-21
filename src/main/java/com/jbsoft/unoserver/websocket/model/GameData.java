package com.jbsoft.unoserver.websocket.model;

import com.jbsoft.unoserver.game.Card;

import java.util.List;

public class GameData {
    private int playerId;
    private Card card;
    private String action;

    public GameData () { }


    public int getPlayerId() {
        return playerId;
    }

    public void setPlayerId(int playerId) {
        this.playerId = playerId;
    }

    public Card getCard() {
        return card;
    }

    public void setCard(Card card) {
        this.card = card;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }
}


