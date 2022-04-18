package com.jbsoft.unoserver.game;

import com.jbsoft.unoserver.Response;

import java.util.ArrayList;
import java.util.List;

public class Game {
    private final String roomId, ownerUserName;
    private final List<Player> players = new ArrayList<>();
    private int playerIdCounter = 1;

    public Game(String roomId, String ownerUserName) {
        this.roomId = roomId;
        this.ownerUserName = ownerUserName;
    }

    public void addPlayer(String username) {
        players.add(new Player(username, playerIdCounter++));
    }

    public void addPlayer(String username, String sessionId) {
        players.add(new Player(username, roomId, sessionId, playerIdCounter++));
    }


    public Player findPlayerByUsername(String username) {
        return players.stream()
                .filter(p -> p.getUsername().equals(username))
                .findFirst()
                .orElse(null);
    }

    public Player findPlayerBySessionId(String sessionId) {
        System.out.println("Looking for " + sessionId);
        System.out.println("Containing");
        for(Player p : players) {
            System.out.println(p.getSessionId());
        }
        return players.stream()
                .filter(p -> p.getSessionId().equals(sessionId))
                .findFirst()
                .orElse(null);
    }

    public void connectPlayerToWebSocket(String username, String roomId, String sessionId) {
        Player player = findPlayerByUsername(username);
        if(player == null) return;
        player.setRoomId(roomId);
        player.setSessionId(sessionId);
    }

    public PlayerConfig getPlayerConfig(String sessionId) {
        return findPlayerBySessionId(sessionId).getConfig();
    }

    public GameState getState() {
        return new GameState(Response.Type.STATE, roomId, ownerUserName, players, GameState.StateType.GLOBAL);
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
}
