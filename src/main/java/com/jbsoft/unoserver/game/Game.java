package com.jbsoft.unoserver.game;

import com.jbsoft.unoserver.Response;
import com.jbsoft.unoserver.websocket.model.GameData;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Stack;

public class Game {
    private final String roomId;
    private final List<Player> players = new ArrayList<>();
    private String ownerUserName;
    private Stack<Card> deck;
    private Stack<Card> playedCards = new Stack<>();
    private int playerIdCounter = 1;
    private boolean running = false;
    private State state = State.NOT_STARTED;
    private int playerTurn = 1;

    public Game(String roomId, String ownerUserName) {
        this.roomId = roomId;
        this.ownerUserName = ownerUserName;
    }

    public void addPlayer(String username, String sessionId) {
        if (ownerUserName == null)
            ownerUserName = username;
        players.add(new Player(username, roomId, sessionId, playerIdCounter++));
    }

    public Player findPlayerByPlayerId(int id) {
        return players.stream()
                .filter(p -> p.getPlayerId() == id)
                .findFirst()
                .orElse(null);
    }

    public Player findPlayerByUsername(String username) {
        return players.stream()
                .filter(p -> p.getUsername().equals(username))
                .findFirst()
                .orElse(null);
    }

    public Player findPlayerBySessionId(String sessionId) {
        return players.stream()
                .filter(p -> p.getSessionId().equals(sessionId))
                .findFirst()
                .orElse(null);
    }

    public void connectPlayerToWebSocket(String username, String roomId, String sessionId) {
        Player player = findPlayerByUsername(username);
        if (player == null) return;
        player.setRoomId(roomId);
        player.setSessionId(sessionId);
    }

    public DataResponse getPlayerConfig(String sessionId) {
        return findPlayerBySessionId(sessionId).getConfig();
    }

    public DataResponse getState() {
        return new DataResponse.ResponseBuilder()
                .type(Response.Type.STATE)
                .roomId(roomId)
                .ownerUsername(ownerUserName)
                .players(players)
                .state(state.toString())
                .playerTurn(playerTurn)
                .gameStarted(running)
                .build();
    }

    public void start() {
        running = true;
        state = State.INITIALIZE;
        try {
            deck = GameDataInit.getDeck();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private List<Card> drawCards(int numberOfCards) {
        List<Card> cards = new ArrayList<>();
        while (numberOfCards-- > 0 && deck.size() > 0) {
            cards.add(deck.pop());
        }
        return cards;
    }

    private List<DataResponse> init() {
        List<DataResponse> responses = new ArrayList<>();
        for (Player p : players) {
            List<Card> cards = drawCards(7);
            p.addCardsToHand(cards);

            System.out.println("cardsize" + cards.size());
            responses.add(new DataResponse.ResponseBuilder()
                    .type(Response.Type.DRAW)
                    .username(p.getUsername())
                    .sessionId(p.getSessionId())
                    .playerId(p.getPlayerId())
                    .cards(cards)
                    .build());
        }
        return responses;
    }

    private boolean validPlay(Player player, Card card) {
        if(playedCards.isEmpty()) return true;
        if(playedCards.peek().getType().equals(Card.Type.DRAW4)) return true;
        if (player.getPlayerId() != playerTurn) return false;
        if(card.getType().equals(Card.Type.DRAW4)) return true;
        return playedCards.peek().getColor().equals(card.getColor()) || Objects.equals(playedCards.peek().getValue(), card.getValue());
    }

    public List<DataResponse> playTurn(GameData data) {
        List<DataResponse> response = new ArrayList<>();
        Player player = findPlayerByPlayerId(data.getPlayerId());
        if (data.getAction() != null && data.getAction().equals("DRAW")) {
            if(player.getPlayerId() != playerTurn) return response;
            List<Card> cards = drawCards(1);
            response.add(new DataResponse.ResponseBuilder()
                    .type(Response.Type.DRAW)
                    .playerId(player.getPlayerId())
                    .roomId(roomId)
                    .sessionId(player.getSessionId())
                    .cards(cards)
                    .build());
            response.add(new DataResponse.ResponseBuilder()
                    .type(Response.Type.STATE)
                    .playerTurn(playerTurn)
                    .state(state.toString())
                    .players(players)
                    .roomId(roomId)
                    .build());
            return response;
        }
        Card card = data.getCard();
        if (!validPlay(player, card)) return response;
        switch (card.getType()) {
            case DRAW2 -> {
                List<Card> cards = drawCards(2);
                Player opponent = findPlayerByPlayerId(player.getPlayerId() == 1 ? 2 : 1);
                response.add(new DataResponse.ResponseBuilder()
                        .type(Response.Type.DRAW)
                        .playerId(opponent.getPlayerId())
                        .roomId(roomId)
                        .sessionId(opponent.getSessionId())
                        .cards(cards)
                        .build());
            }
            case DRAW4 -> {
                List<Card> cards = drawCards(4);
                Player opponent = findPlayerByPlayerId(player.getPlayerId() == 1 ? 2 : 1);
                response.add(new DataResponse.ResponseBuilder()
                        .type(Response.Type.DRAW)
                        .playerId(opponent.getPlayerId())
                        .roomId(roomId)
                        .sessionId(opponent.getSessionId())
                        .cards(cards)
                        .build());
            }
            case NUMBER, REVERSE -> {

            }
            case SKIP -> {
                playerTurn = playerTurn == 1 ? 2 : 1;
            }
        }
        player.getHand().remove(card);
        response.add(new DataResponse.ResponseBuilder()
                .type(Response.Type.MOVE)
                .card(card)
                .playerId(player.getPlayerId())
                .roomId(roomId)
                .build());
        playerTurn = playerTurn == 1 ? 2 : 1;
        response.add(new DataResponse.ResponseBuilder()
                .type(Response.Type.STATE)
                .playerTurn(playerTurn)
                .state(state.toString())
                .players(players)
                .roomId(roomId)
                .build());
        playedCards.add(card);
        return response;
    }

    private List<DataResponse> generateResponse() {
        return switch (state) {
            case NOT_STARTED -> null;
            case INITIALIZE -> init();
        };
    }

    public List<DataResponse> getResponse() {
        return generateResponse();
    }

    public String getRoomId() {
        return roomId;
    }

    public String getOwnerUserName() {
        return ownerUserName;
    }

    public List<Player> getPlayers() {
        return players;
    }

    private enum State {
        NOT_STARTED,
        INITIALIZE,
    }
}
