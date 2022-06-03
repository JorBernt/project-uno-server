package com.jbsoft.unoserver.rest.controller;

import com.jbsoft.unoserver.GlobalVariables;
import com.jbsoft.unoserver.game.services.GameManager;
import com.jbsoft.unoserver.rest.model.BoolResponse;
import com.jbsoft.unoserver.rest.model.RoomId;
import org.springframework.web.bind.annotation.*;

@RestController
public class ClientController {
    @CrossOrigin(origins = GlobalVariables.ALLOWED_ORIGIN)
    @GetMapping("/createNewGame")
    public RoomId createNewGame(String sessionId, boolean bot) {
        System.out.println(bot);
        String id = GameManager.the().createNewGame(null, sessionId, bot);
        return new RoomId(id);
    }

    @CrossOrigin(origins = GlobalVariables.ALLOWED_ORIGIN)
    @GetMapping("/joinGame")
    public BoolResponse joinGame(String username, String roomId, String sessionId) {
        boolean joined = GameManager.the().joinGame(username, roomId, sessionId);
        return new BoolResponse(joined);
    }

    @CrossOrigin(origins = GlobalVariables.ALLOWED_ORIGIN)
    @GetMapping("/checkValidSessionId")
    public BoolResponse checkValidSessionId(String roomId, String sessionId) {
        boolean joined = GameManager.the().containsSessionId (roomId, sessionId);
        return new BoolResponse(joined);
    }

    @CrossOrigin(origins = GlobalVariables.ALLOWED_ORIGIN)
    @GetMapping("/checkValidRoomId")
    public BoolResponse checkValidRoomId(String roomId) {
        boolean joined = GameManager.the().containsRoom(roomId);
        return new BoolResponse(joined);
    }

}
