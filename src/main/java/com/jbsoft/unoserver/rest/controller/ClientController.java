package com.jbsoft.unoserver.rest.controller;

import com.jbsoft.unoserver.game.services.GameManager;
import com.jbsoft.unoserver.rest.model.BoolResponse;
import com.jbsoft.unoserver.rest.model.RoomId;
import org.springframework.web.bind.annotation.*;

@RestController
public class ClientController {

    @CrossOrigin(origins = "http://localhost:3000")
    @GetMapping("/createNewGame")
    public RoomId createNewGame(String sessionId) {
        String id = GameManager.the().createNewGame(null, sessionId);
        return new RoomId(id);
    }

    @CrossOrigin(origins = "http://localhost:3000")
    @GetMapping("/joinGame")
    public BoolResponse joinGame(String username, String roomId, String sessionId) {
        boolean joined = GameManager.the().joinGame(username, roomId, sessionId);
        return new BoolResponse(joined);
    }

    @CrossOrigin(origins = "http://localhost:3000")
    @GetMapping("/checkValidSessionId")
    public BoolResponse checkValidSessionId(String roomId, String sessionId) {
        boolean joined = GameManager.the().containsSessionId (roomId, sessionId);
        return new BoolResponse(joined);
    }

    @CrossOrigin(origins = "http://localhost:3000")
    @GetMapping("/checkValidRoomId")
    public BoolResponse checkValidRoomId(String roomId) {
        boolean joined = GameManager.the().containsRoom(roomId);
        return new BoolResponse(joined);
    }

}
