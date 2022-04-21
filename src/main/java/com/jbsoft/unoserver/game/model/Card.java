package com.jbsoft.unoserver.game.model;


import java.util.Objects;

public class Card  {
    private Color color;
    private String value;
    private Type type;
    private String string;
    private int id;

    public Card () { }

    public Card (int keygen) { id = keygen; }

    public Card(Type type, int keygen) {
        this(keygen);
        this.type = type;
        stringify();
    }

    public Card(Type type, Color color, String value, int keygen) {
        this(keygen);
        this.type = type;
        this.color = color;
        this.value = value;
        stringify();
    }

    private void stringify() {
        if (type.equals(Type.DRAW4)) {
            string = Type.DRAW4.toString();
        } else if (!type.equals(Type.NUMBER)) {
            string = String.format("%s_%s", type, color);
            ;
        } else {
            string = String.format("%s_%s_%s", type, color, value);
        }
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public String getString() {
        return string;
    }

    public void setString(String string) {
        this.string = string;
    }

    public void hideData() {

    }

    public enum Type {
        DRAW2,
        DRAW4,
        NUMBER,
        REVERSE,
        SKIP,
        WILD
    }

    public enum Color {
        RED,
        YELLOW,
        BLUE,
        GREEN,
        BLACK
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Card card = (Card) o;
        return Objects.equals(string, card.string);
    }

    @Override
    public int hashCode() {
        return Objects.hash(string);
    }
}


