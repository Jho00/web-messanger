package com.akutin.messaginglogic.controller;

import com.akutin.messaginglogic.common.constatns.HeaderConstants;
import com.akutin.messaginglogic.common.exceptions.NotFoundInDbException;
import com.akutin.messaginglogic.common.requests.ThreadCreateModel;
import com.akutin.messaginglogic.services.IdentityService;
import com.akutin.messaginglogic.services.ThreadService;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

@RestController
public class ThreadsControllers {
    private final ThreadService threadService;
    private final IdentityService identityService;

    public ThreadsControllers(ThreadService threadService, IdentityService identityService) {
        this.threadService = threadService;
        this.identityService = identityService;
    }

    @GetMapping(path = "/threads/")
    @CrossOrigin(origins = "*")
    public @ResponseBody
    Object getThread(HttpServletResponse http,
                     @RequestHeader(HeaderConstants.AUTH_HEAD) String authKey,
                     @RequestParam(required = false) Integer threadId) {
        if (!identityService.isAuthenticated(authKey)) {
            http.setStatus(401);
            return null;
        }

        if (threadId != null) {
            try {
                return threadService.getThread(threadId);
            } catch (NotFoundInDbException e) {
                http.setStatus(404);
                return null;
            }
        } else {
            return threadService.getAllThreads();
        }
    }

    @PostMapping(path = "/threads/create")
    @CrossOrigin(origins = "*")
    public @ResponseBody
    boolean threadCreate(
            HttpServletResponse http,
            @RequestHeader(HeaderConstants.AUTH_HEAD) String authKey,
            @RequestBody ThreadCreateModel threadCreateModel
            ) {
        if (!identityService.isAuthenticated(authKey)) {
            http.setStatus(401);
            return false;
        }

        threadService.create(threadCreateModel);
        http.setStatus(201);
        return true;
    }

    @PostMapping(path = "/threads/seen")
    @CrossOrigin(origins = "*")
    public @ResponseBody
    boolean makeSeenThread(
            HttpServletResponse http,
            @RequestHeader(HeaderConstants.AUTH_HEAD) String authKey,
            @RequestParam(required = true) Integer threadId
    ) {
        if (!identityService.isAuthenticated(authKey)) {
            http.setStatus(401);
            return false;
        }

        return threadService.makeSeenThread(threadId);
    }
}
