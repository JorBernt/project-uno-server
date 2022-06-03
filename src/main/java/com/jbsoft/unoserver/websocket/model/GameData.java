package com.jbsoft.unoserver.websocket.model;

import com.jbsoft.unoserver.game.model.Card;

public class GameData {
    private int playerId;
    private Card card;
    private String chosenColor;
    private String action;
    private String sessionId;

    public GameData () { }

    public GameData(int playerId, Card card, String chosenColor, String action, String sessionId) {
        this.playerId = playerId;
        this.card = card;
        this.chosenColor = chosenColor;
        this.action=action;
        this.sessionId = sessionId;
    }

    public GameData(int playerId, Card card, String chosenColor, String action) {
        this.playerId = playerId;
        this.card = card;
        this.chosenColor = chosenColor;
        this.action=action;
    }


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

    public String getChosenColor() {
        return chosenColor;
    }

    public void setChosenColor(String chosenColor) {
        this.chosenColor = chosenColor;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }
}


