package com.jbsoft.unoserver.game.services;

import com.jbsoft.unoserver.game.model.Card;
import com.jbsoft.unoserver.game.model.Player;
import com.jbsoft.unoserver.game.model.ResponseImpl;
import com.jbsoft.unoserver.websocket.model.GameData;

import java.util.*;

public class Bot extends Player {

    public Bot(String username, int playerId) {
        super(username, playerId);
    }

    public List<ResponseImpl> playTurn(Game game) {
        try {
            Thread.sleep(500);
        }
        catch (Exception e) { }
        Card card = null;
        String chosenColor = "";
        for (Card c : getHand()) {
            if (game.validPlay(this, c)) {
                card = c;
                if(c.getType().equals(Card.Type.WILD) || c.getType().equals(Card.Type.DRAW4)) {
                    Map<String, Integer> colors = new HashMap<>();
                    int max = 0;
                    for(Card cc : getHand()) {
                        colors.put(cc.getColor().toString(), colors.getOrDefault(cc.getColor().toString(), 0) + 1);
                        max = Math.max(colors.get(cc.getColor().toString()), max);
                    }
                    final int finalMax = max;
                    chosenColor = colors.entrySet().stream().filter(e -> e.getValue() == finalMax).findFirst().get().getKey();
                    System.out.println("Chosen color " + chosenColor);
                }
                break;
            }
        }
        if(card == null) {
            System.out.println("No play");
            List<ResponseImpl> responses = new ArrayList<>(game.playTurn(new GameData(getPlayerId(), null, null, "DRAW")));
            responses.addAll(playTurn(game));
            return responses;
        }
        return game.playTurn(new GameData(getPlayerId(), card, chosenColor, "MOVE"));
    }

}
