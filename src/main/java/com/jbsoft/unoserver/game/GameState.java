package com.jbsoft.unoserver.game;

import com.jbsoft.unoserver.Response;

import java.util.List;

public class GameState extends Response {
    private String roomId, ownerUserName;
    private List<Player> players;
    private StateType stateType;

    enum StateType {
        LOCAL,
        GLOBAL
    }

    public GameState () { super(null); }

    public GameState(Response.Type type, String roomId, String ownerUserName, List<Player> players, StateType stateType) {
        super(type);
        this.roomId = roomId;
        this.ownerUserName = ownerUserName;
        this.players = players;
        this.stateType = stateType;
    }

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public String getOwnerUserName() {
        return ownerUserName;
    }

    public void setOwnerUserName(String ownerUserName) {
        this.ownerUserName = ownerUserName;
    }

    public List<Player> getPlayers() {
        return players;
    }

    public void setPlayers(List<Player> players) {
        this.players = players;
    }

    public StateType getStateType() {
        return stateType;
    }

    public void setStateType(StateType stateType) {
        this.stateType = stateType;
    }

}
