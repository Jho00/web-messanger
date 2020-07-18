package com.akutin.messaginglogic.controller;

import com.akutin.messaginglogic.common.constatns.HeaderConstants;
import com.akutin.messaginglogic.common.requests.SendMessageModel;
import com.akutin.messaginglogic.services.IdentityService;
import com.akutin.messaginglogic.services.MessagesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

@RestController
public class MessageController {
    private final IdentityService identityService;
    private final MessagesService messagesService;

    public MessageController(IdentityService identityService, MessagesService messagesService) {
        this.identityService = identityService;
        this.messagesService = messagesService;
    }


    @PostMapping(path = "/messages/send")
    @CrossOrigin(origins = "*")
    public boolean sendMessage(
            HttpServletResponse http,
            @RequestHeader(HeaderConstants.AUTH_HEAD) String authKey,
            @RequestBody SendMessageModel model
            ) {
        if (!identityService.isAuthenticated(authKey)) {
            http.setStatus(401);
            return false;
        }

        messagesService.sendMessage(model);
        http.setStatus(201);
        return true;
    }
}
