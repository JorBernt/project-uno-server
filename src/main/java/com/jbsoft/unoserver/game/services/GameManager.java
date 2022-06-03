package com.jbsoft.unoserver.game.services;

import com.jbsoft.unoserver.game.model.ResponseImpl;
import com.jbsoft.unoserver.websocket.model.GameData;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import static com.jbsoft.unoserver.utility.Randomizer.generateID;

public class GameManager {
    private static GameManager the = null;

    private static final Map<String, Game> runningGames = new HashMap<>();

    public GameManager() {
    }

    public static GameManager the() {
        return the = the == null ? new GameManager() : the;
    }

    public String createNewGame(String ownerUserName, String sessionId, boolean bot) {
        String roomId = generateID();
        while (runningGames.containsKey(roomId)) {
            roomId = generateID();
        }
        Game game = new Game(roomId, ownerUserName, bot);
        runningGames.put(roomId, game);
        System.out.println(runningGames.size());
        return roomId;
    }

    public boolean joinGame(String username, String roomId, String sessionId) {
        Game game = runningGames.get(roomId);
        if (game == null) return false;
        game.addPlayer(username, sessionId);
        return true;
    }

    public ResponseImpl getState(String roomId) {
        Game game = runningGames.get(roomId);
        if (game == null) {
            return null;
        }
        return game.getState();
    }

    public List<ResponseImpl> updateState(String roomId, GameData data) {
        Game game = runningGames.get(roomId);
        if (game == null) return null;
        return game.playTurn(data);
    }

    public ResponseImpl connectPlayer(String username, String roomId, String sessionId) {
        Game game = runningGames.get(roomId);
        if (game == null) return null;
        game.connectPlayerToWebSocket(username, roomId, sessionId);
        return game.getPlayerConfig(sessionId);
    }

    public ResponseImpl getPlayerConfig(String roomId, String sessionId) {
        Game game = runningGames.get(roomId);
        if (game == null) return null;
        return game.getPlayerConfig(sessionId);
    }

    public boolean containsSessionId(String roomId, String sessionId) {
        Game game = runningGames.get(roomId);
        if (game == null) return false;
        return game.findPlayerBySessionId(sessionId) != null;
    }

    public boolean containsRoom(String roomId) {
        return runningGames.containsKey(roomId);
    }

    public List<ResponseImpl> startGame(String roomId) {
        Game game = runningGames.get(roomId);
        if (game == null) return null;
        game.start();
        return game.getResponse();
    }
}
