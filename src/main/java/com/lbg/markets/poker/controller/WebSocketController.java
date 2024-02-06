package com.lbg.markets.poker.controller;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class WebSocketController {

    @MessageMapping("/leaderAction")
    @SendTo("/topic/redirect")
    public String handleLeaderAction(String pageUrl) {
        // Logic to handle leader's action
        return pageUrl; // URL to redirect other users to
    }

}

