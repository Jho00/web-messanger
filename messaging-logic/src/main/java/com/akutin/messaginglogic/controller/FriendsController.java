package com.akutin.messaginglogic.controller;

import com.akutin.messaginglogic.common.constatns.HeaderConstants;
import com.akutin.messaginglogic.common.views.SettingsView;
import com.akutin.messaginglogic.entity.FriendRequest;
import com.akutin.messaginglogic.entity.User;
import com.akutin.messaginglogic.services.IdentityService;
import com.akutin.messaginglogic.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.web.bind.annotation.*;
import org.springframework.mail.javamail.JavaMailSender;
import javax.servlet.http.HttpServletResponse;
import java.util.HashSet;
import java.util.Set;

@RestController
public class FriendsController {
    private final UserService userService;
    private final IdentityService identityService;

    public FriendsController(UserService userService, IdentityService identityService) {
        this.userService = userService;
        this.identityService = identityService;
    }

    @GetMapping(path = "/friends/")
    @CrossOrigin(origins = "*")
    public Set<SettingsView> getFriends(
            HttpServletResponse http,
            @RequestHeader(HeaderConstants.AUTH_HEAD) String authKey
    ) {
        if (!identityService.isAuthenticated(authKey)) {
            http.setStatus(401);
            return null;
        }

        Set<User> friends =  identityService.getUser().getFriends();

        Set<SettingsView> views = new HashSet<>();
        friends.forEach(item -> {
            SettingsView view = new SettingsView(item);
            view.setFriended(true);
            views.add(view);
        });

        return views;
    }

    @PostMapping(path = "/friends/add")
    @CrossOrigin(origins = "*")
    public Boolean addFriend(
            HttpServletResponse http,
            @RequestHeader(HeaderConstants.AUTH_HEAD) String authKey,
            @RequestParam Integer id
    ) {
        if (!identityService.isAuthenticated(authKey)) {
            http.setStatus(401);
            return false;
        }

        return userService.addFriend(id);
    }

    @DeleteMapping(path = "/friends/delete")
    @CrossOrigin(origins = "*")
    public Boolean deleteFriend(
            HttpServletResponse http,
            @RequestHeader(HeaderConstants.AUTH_HEAD) String authKey,
            @RequestParam Integer id
    ) {
        if (!identityService.isAuthenticated(authKey)) {
            http.setStatus(401);
            return false;
        }

        return userService.deleteFriend(id);
    }

    @GetMapping(path = "/friends/reqs")
    @CrossOrigin(origins = "*")
    public Iterable<FriendRequest> getRequests(
            HttpServletResponse http,
            @RequestHeader(HeaderConstants.AUTH_HEAD) String authKey
    ) {
        if (!identityService.isAuthenticated(authKey)) {
            http.setStatus(401);
            return null;
        }

        return userService.getFriendRequests();
    }

    @PostMapping(path = "/friends/accept-request")
    @CrossOrigin(origins = "*")
    public Boolean acceptFriendRequest(
            HttpServletResponse http,
            @RequestHeader(HeaderConstants.AUTH_HEAD) String authKey,
            @RequestParam Integer requestId
    ) {
        if (!identityService.isAuthenticated(authKey)) {
            http.setStatus(401);
            return false;
        }

        return userService.acceptRequest(requestId);
    }

    @PostMapping(path = "/friends/decline-request")
    @CrossOrigin(origins = "*")
    public Boolean declineFriendRequest(
            HttpServletResponse http,
            @RequestHeader(HeaderConstants.AUTH_HEAD) String authKey,
            @RequestParam Integer requestId
    ) {
        if (!identityService.isAuthenticated(authKey)) {
            http.setStatus(401);
            return false;
        }

        return userService.declineRequest(requestId);
    }
}
