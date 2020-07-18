package com.akutin.messaginglogic.controller;

import com.akutin.messaginglogic.common.constatns.HeaderConstants;
import com.akutin.messaginglogic.common.exceptions.NotFoundInDbException;
import com.akutin.messaginglogic.common.views.SettingsView;
import com.akutin.messaginglogic.common.views.UserView;
import com.akutin.messaginglogic.entity.FriendRequest;
import com.akutin.messaginglogic.entity.User;
import com.akutin.messaginglogic.repository.FriendRequestRepository;
import com.akutin.messaginglogic.services.IdentityService;
import com.akutin.messaginglogic.services.UserService;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.Optional;

@RestController
public class UserController {
    private final UserService userService;
    private final IdentityService identityService;

    private final FriendRequestRepository friendRequestRepository;

    public UserController(UserService userService, IdentityService identityService, FriendRequestRepository friendRequestRepository) {
        this.userService = userService;
        this.identityService = identityService;

        this.friendRequestRepository = friendRequestRepository;
    }

    @GetMapping(path = "/users/find")
    @CrossOrigin(origins = "*")
    public Iterable<UserView> find(
            HttpServletResponse http,
            @RequestHeader(HeaderConstants.AUTH_HEAD) String authKey,
            @RequestParam String q
    ) {
        if (!identityService.isAuthenticated(authKey)) {
            http.setStatus(401);
            return null;
        }

        return userService.find(q);
    }

    @GetMapping(path = "/users/me")
    @CrossOrigin(origins = "*")
    public UserView fetchProfile(HttpServletResponse http,
                                 @RequestHeader(HeaderConstants.AUTH_HEAD) String authKey) {
        if (!identityService.isAuthenticated(authKey)) {
            http.setStatus(401);
            return null;
        }

        return identityService.getUserView();
    }

    @GetMapping(path = "/users/profile")
    @CrossOrigin(origins = "*")
    public SettingsView getProfile(
            HttpServletResponse http,
            @RequestHeader(HeaderConstants.AUTH_HEAD) String authKey,
            @RequestParam(required = false) Integer id,
            @RequestParam(required = false) String nickname
    ) {
        if (!identityService.isAuthenticated(authKey)) {
            http.setStatus(401);
            return null;
        }

        if (id == null && nickname == null) {
            http.setStatus(400);
            return null;
        }

        User me = identityService.getUser();
        if (me.getId().equals(id) || me.getNickname().toLowerCase().equals(nickname == null ? null : nickname.toLowerCase())) {
            http.setStatus(403);
            return null;
        }

        try {
            User user = userService.getProfile(id, nickname);
            Optional<FriendRequest> friendRequestOptional = friendRequestRepository.findByTargetIdAndOwnerId(user.getId(), identityService.getUserId());
            return new SettingsView(user, identityService.getUser(), friendRequestOptional.isPresent());
        } catch (NotFoundInDbException e) {
            http.setStatus(404);
            return null;
        }
    }

    @GetMapping(path = "/users/resetPassword")
    @CrossOrigin(origins = "*")
    public Boolean resetPassword(
            HttpServletResponse http,
            @RequestHeader(HeaderConstants.AUTH_HEAD) String authKey,
            @RequestParam(required = false) Integer id
    ) {
        if (!identityService.isAuthenticated(authKey)) {
            http.setStatus(401);
            return false;
        }

        if (!identityService.getUser().isAdmin()) {
            http.setStatus(403);
            return false;
        }

        return userService.resetPassword(id);
    }
}
