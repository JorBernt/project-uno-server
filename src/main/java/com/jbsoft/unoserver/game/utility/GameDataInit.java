package com.jbsoft.unoserver.game.utility;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jbsoft.unoserver.game.model.Card;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.Stack;

public class GameDataInit {
    private static final ObjectMapper mapper = new ObjectMapper();
    private static int keygen = 0;

   /* public static void main(String[] args) {
        try {
            getDeck();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
*/


    public static Stack<Card> getDeck() throws IOException {
        //Card generator
        Stack <Card> deck = new Stack<>();
        /*or (Card.Color c : Card.Color.values()) {
            if (c.equals(Card.Color.BLACK)) continue;
            deck.add(new Card(Card.Type.NUMBER, c, "0", keygen++));
        }
        for (int i = 1; i <= 9; i++) {
            for (Card.Color c : Card.Color.values()) {
                if (c.equals(Card.Color.BLACK)) continue;
                for (int j = 0; j < 2; j++) {
                    deck.add(new Card(Card.Type.NUMBER, c, Integer.toString(i), keygen++));
                }
            }
        }
        for (Card.Color c : Card.Color.values()) {
            if (c.equals(Card.Color.BLACK)) continue;
            for (int j = 0; j < 2; j++) {
                deck.add(new Card(Card.Type.SKIP, c, "S", keygen++));
                deck.add(new Card(Card.Type.REVERSE, c, "R", keygen++));
                deck.add(new Card(Card.Type.DRAW2, c, "2+", keygen++));
            }
        }
        for (int i = 0; i < 4; i++) {
            deck.add(new Card(Card.Type.DRAW4, Card.Color.BLACK, "4+", keygen++));
            deck.add(new Card(Card.Type.WILD, Card.Color.BLACK, "W", keygen++));
        }
        mapper.writeValue(new File("src/main/resources/deck.json"), deck);*/
        deck = mapper.readValue(new File("src/main/resources/deck.json"), new TypeReference<>() {});
        Collections.shuffle(deck);
        return deck;
    }


}
