package com.jbsoft.unoserver;

abstract public class Response {

    public enum Type {
        MOVE,
        STATE,
        CONFIG,
        DRAW
    }
    protected Type type;

    public Response(Type type) { this.type = type; }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }
}
