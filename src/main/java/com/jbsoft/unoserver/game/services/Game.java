package com.jbsoft.unoserver.game.services;

import com.jbsoft.unoserver.Response;
import com.jbsoft.unoserver.game.utility.GameDataInit;
import com.jbsoft.unoserver.game.model.Card;
import com.jbsoft.unoserver.game.model.ResponseImpl;
import com.jbsoft.unoserver.game.model.Player;
import com.jbsoft.unoserver.websocket.model.GameData;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Stack;

public class Game {
    private final String roomId;
    private final List<Player> players = new ArrayList<>();
    private final Stack<Card> playedCards = new Stack<>();
    private String ownerUserName;
    private Stack<Card> deck;
    private int playerIdCounter = 1;
    private boolean running = false;
    private State state = State.NOT_STARTED;
    private int playerTurn = 1;
    private Direction direction = Direction.LEFT;
    private Color wildChosenColor = Color.NONE;

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

    public ResponseImpl getPlayerConfig(String sessionId) {
        return findPlayerBySessionId(sessionId).getConfig();
    }

    public ResponseImpl getState() {
        return new ResponseImpl.ResponseBuilder()
                .type(Response.Type.STATE)
                .roomId(roomId)
                .ownerUsername(ownerUserName)
                .players(players)
                .state(state.toString())
                .playerTurn(playerTurn)
                .gameStarted(running)
                .wildChosenColor(wildChosenColor.toString())
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

    private List<ResponseImpl> init() {
        List<ResponseImpl> responses = new ArrayList<>();
        for (Player p : players) {
            List<Card> cards = drawCards(7);
            p.addCardsToHand(cards);
            cards.add(deck.stream().filter(c -> c.getType().equals(Card.Type.DRAW4)).findFirst().get());

            System.out.println("cardsize" + cards.size());
            responses.add(new ResponseImpl.ResponseBuilder()
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
        if (card.getType().equals(Card.Type.DRAW4) || card.getType().equals(Card.Type.WILD))
            return true;
        if (playedCards.isEmpty())
            return true;
        if (wildChosenColor != Color.NONE && !card.getColor().toString().equals(wildChosenColor.toString()))
            return false;
        if (wildChosenColor != Color.NONE && card.getColor().toString().equals(wildChosenColor.toString()))
            return true;
        if (player.getPlayerId() != playerTurn)
            return false;

        return playedCards.peek().getColor().equals(card.getColor()) || Objects.equals(playedCards.peek().getValue(), card.getValue());
    }

    private boolean canDrawFromDeck(Player player) {
        for (Card c : player.getHand()) {
            if (validPlay(player, c)) return false;
        }
        return true;
    }

    public List<ResponseImpl> playTurn(GameData data) {
        List<ResponseImpl> response = new ArrayList<>();
        Player player = findPlayerByPlayerId(data.getPlayerId());
        if (data.getAction() != null && data.getAction().equals("DRAW")) {
            if (player.getPlayerId() != playerTurn || !canDrawFromDeck(player)) return response;
            List<Card> cards = drawCards(1);
            player.getHand().addAll(cards);
            playerTurn = nextPlayerId(player);
            response.add(new ResponseImpl.ResponseBuilder()
                    .type(Response.Type.DRAW)
                    .playerId(player.getPlayerId())
                    .roomId(roomId)
                    .sessionId(player.getSessionId())
                    .cards(cards)
                    .build());
            response.add(new ResponseImpl.ResponseBuilder()
                    .type(Response.Type.STATE)
                    .playerTurn(playerTurn)
                    .state(state.toString())
                    .players(players)
                    .wildChosenColor(wildChosenColor.toString())
                    .roomId(roomId)
                    .build());
            return response;
        }
        Card card = data.getCard();
        if (!validPlay(player, card)) return response;
        switch (card.getType()) {
            case DRAW2 -> {
                wildChosenColor = Color.NONE;
                List<Card> cards = drawCards(2);
                Player opponent = findPlayerByPlayerId(nextPlayerId(player));
                opponent.getHand().addAll(cards);
                playerTurn = nextPlayerId(player);
                response.add(new ResponseImpl.ResponseBuilder()
                        .type(Response.Type.DRAW)
                        .playerId(opponent.getPlayerId())
                        .roomId(roomId)
                        .sessionId(opponent.getSessionId())
                        .cards(cards)
                        .build());
            }
            case WILD -> {
                wildChosenColor = Color.valueOf(data.getChosenColor());
            }
            case DRAW4 -> {
                List<Card> cards = drawCards(4);
                Player opponent = findPlayerByPlayerId(nextPlayerId(player));
                opponent.getHand().addAll(cards);
                wildChosenColor = Color.valueOf(data.getChosenColor());
                playerTurn = nextPlayerId(player);
                response.add(new ResponseImpl.ResponseBuilder()
                        .type(Response.Type.DRAW)
                        .playerId(opponent.getPlayerId())
                        .roomId(roomId)
                        .sessionId(opponent.getSessionId())
                        .cards(cards)
                        .build());
            }
            case NUMBER -> {
                wildChosenColor = Color.NONE;
            }

            case REVERSE -> {
                wildChosenColor = Color.NONE;
                direction = direction == Direction.LEFT ? Direction.RIGHT : Direction.LEFT;
            }

            case SKIP -> {
                wildChosenColor = Color.NONE;
                playerTurn = nextPlayerId(player);
            }
        }
        player.getHand().remove(card);
        response.add(new ResponseImpl.ResponseBuilder()
                .type(Response.Type.MOVE)
                .card(card)
                .playerId(player.getPlayerId())
                .roomId(roomId)
                .build());
        playerTurn = nextPlayerId(findPlayerByPlayerId(playerTurn));
        response.add(new ResponseImpl.ResponseBuilder()
                .type(Response.Type.STATE)
                .playerTurn(playerTurn)
                .state(state.toString())
                .players(players)
                .roomId(roomId)
                .wildChosenColor(wildChosenColor.toString())
                .build());
        playedCards.add(card);
        return response;
    }

    private int nextPlayerId(Player player) {
        int nextPlayerId = (player.getPlayerId() + direction.val);
        if (nextPlayerId <= 0) nextPlayerId = players.size();
        else if (nextPlayerId > players.size()) nextPlayerId = 1;
        return nextPlayerId;
    }

    private List<ResponseImpl> generateResponse() {
        return switch (state) {
            case NOT_STARTED -> null;
            case INITIALIZE -> init();
        };
    }

    public List<ResponseImpl> getResponse() {
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

    enum Color {
        NONE,
        RED,
        BLUE,
        GREEN,
        YELLOW;
    }

    public enum Direction {
        LEFT(-1),
        RIGHT(1);
        public final int val;

        Direction(int val) {
            this.val = val;
        }
    }


    private enum State {
        NOT_STARTED,
        INITIALIZE,
    }
}
