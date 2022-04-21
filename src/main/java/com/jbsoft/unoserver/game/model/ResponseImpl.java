package com.jbsoft.unoserver.game.model;

import com.jbsoft.unoserver.Response;

import java.util.ArrayList;
import java.util.List;

public class ResponseImpl extends Response {
    private String username, roomId, sessionId, ownerUsername, state, wildChosenColor;
    private List<Card> cards;
    private List<Player> players;
    private Card card;
    private int playerId, playerTurn;
    private boolean gameStarted;

    private ResponseImpl(ResponseBuilder builder) {
        super(builder.type);
        username = builder.username;
        roomId = builder.roomId;
        sessionId = builder.sessionId;
        card = builder.card;
        cards = builder.cards;
        playerId = builder.playerId;
        ownerUsername = builder.ownerUsername;
        players = builder.players;
        state = builder.state;
        playerTurn = builder.playerTurn;
        gameStarted = builder.gameStarted;
        wildChosenColor = builder.wildChosenColor;
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

    public Card getCard() {
        return card;
    }

    public void setCard(Card card) {
        this.card = card;
    }

    public String getOwnerUsername() {
        return ownerUsername;
    }

    public void setOwnerUsername(String ownerUsername) {
        this.ownerUsername = ownerUsername;
    }

    public List<Card> getCards() {
        return cards;
    }

    public void setCards(List<Card> cards) {
        this.cards = cards;
    }

    public List<Player> getPlayers() {
        return players;
    }

    public void setPlayers(List<Player> players) {
        this.players = players;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public int getPlayerTurn() {
        return playerTurn;
    }

    public void setPlayerTurn(int playerTurn) {
        this.playerTurn = playerTurn;
    }

    public boolean isGameStarted() {
        return gameStarted;
    }

    public void setGameStarted(boolean gameStarted) {
        this.gameStarted = gameStarted;
    }

    public String getWildChosenColor() {
        return wildChosenColor;
    }

    public void setWildChosenColor(String wildChosenColor) {
        this.wildChosenColor = wildChosenColor;
    }

    public void hideCardData() {
        List<Card> newCardList = new ArrayList<>();
        for(Card c : cards) newCardList.add(new Card(c.getId()+100));
        cards = newCardList;
    }

    public static class ResponseBuilder {
        private String username, roomId, sessionId, ownerUsername, state, wildChosenColor;
        private List<Card> cards;
        private List<Player> players;
        private Type type;
        private Card card;
        private boolean gameStarted = true;
        private int playerId, playerTurn;

        public ResponseBuilder() { }

        public ResponseBuilder username(String username) {
            this.username = username;
            return this;
        }

        public ResponseBuilder roomId(String roomId) {
            this.roomId = roomId;
            return this;
        }

        public ResponseBuilder sessionId(String sessionId) {
            this.sessionId = sessionId;
            return this;
        }

        public ResponseBuilder card(Card card) {
            this.card = card;
            return this;
        }

        public ResponseBuilder cards(List<Card> cards) {
            this.cards = cards;
            return this;
        }

        public ResponseBuilder playerId(int playerId) {
            this.playerId = playerId;
            return this;
        }

        public ResponseBuilder type(Type type) {
            this.type = type;
            return this;
        }

        public ResponseBuilder ownerUsername(String ownerUsername) {
            this.ownerUsername = ownerUsername;
            return this;
        }

        public ResponseBuilder players(List<Player> players) {
            this.players = players;
            return this;
        }

        public ResponseBuilder gameStarted(boolean gameStarted) {
            this.gameStarted = gameStarted;
            return this;
        }

        public ResponseBuilder state(String state) {
            this.state = state;
            return this;
        }

        public ResponseBuilder playerTurn(int playerTurn) {
            this.playerTurn = playerTurn;
            return this;
        }

        public ResponseBuilder wildChosenColor(String wildChosenColor) {
            this.wildChosenColor = wildChosenColor;
            return this;
        }

        public ResponseImpl build() {
            return new ResponseImpl(this);
        }

    }
}
