package com.jbsoft.unoserver.game;

import com.jbsoft.unoserver.Response;
import com.jbsoft.unoserver.websocket.model.GameData;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class GameManager {
    private static GameManager the = null;

    private static Map<String, Game> runningGames = new HashMap<>();
    private final Random random = new Random();

    public GameManager() { }

    public static GameManager the() {
        return the == null ? new GameManager() : the;
    }

    public String createNewGame(String ownerUserName, String sessionId) {
        String roomId = generateID();
        Game game = new Game(roomId, ownerUserName);
        game.addPlayer(ownerUserName, sessionId);
        runningGames.put(roomId, game);
        System.out.println(runningGames.size());
        return roomId;
    }

    public boolean joinGame(String username, String roomId, String sessionId) {
        Game game = runningGames.get(roomId);
        if(game == null) return false;
        game.addPlayer(username, sessionId);
        return true;
    }

    public String generateID() {
        int leftLimit = 65; // letter 'a'
        int rightLimit = 122; // letter 'z'
        int targetStringLength = 5;

        String generatedString = random.ints(leftLimit, rightLimit + 1)
                .filter(a -> a < 91 || a > 96)
                .limit(targetStringLength)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();

        if (runningGames.containsKey(generatedString)) return generateID();
        return generatedString;
    }

    public GameState getState(String roomId) {
        Game game = runningGames.get(roomId);
        if(game == null) {
            return null;
        }
        return game.getState();
    }

    public GamePlayerMove updateState(String roomId, GameData data) {
        Game game = runningGames.get(roomId);
        if(game == null) return null;
        //TODO: Implement
        return new GamePlayerMove(Response.Type.MOVE, data.getPlayerId(), "g4");
    }

    public PlayerConfig connectPlayer(String username, String roomId, String sessionId) {
        Game game = runningGames.get(roomId);
        if(game == null) return null;
        game.connectPlayerToWebSocket(username, roomId, sessionId);
        return game.getPlayerConfig(sessionId);
    }

    public PlayerConfig getPlayerConfig(String roomId, String sessionId) {
        Game game = runningGames.get(roomId);
        if(game == null) return null;
        return game.getPlayerConfig(sessionId);
    }

    public boolean containsSessionId(String roomId, String sessionId) {
        Game game = runningGames.get(roomId);
        if(game == null) return false;
        return game.findPlayerBySessionId(sessionId) != null;
    }
}
