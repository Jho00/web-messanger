package com.akutin.messaginglogic.controller;

import com.akutin.messaginglogic.common.constatns.HeaderConstants;
import com.akutin.messaginglogic.common.views.SettingsView;
import com.akutin.messaginglogic.services.IdentityService;
import com.akutin.messaginglogic.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

@RestController
public class SettingController {
    private final IdentityService identityService;

    private final UserService userService;

    SettingController(IdentityService identityService, UserService userService) {
        this.identityService = identityService;
        this.userService = userService;
    }

    @CrossOrigin(origins = "*")
    @GetMapping(path = "/settings/")
    public @ResponseBody SettingsView getAllSettings(
            @RequestHeader(HeaderConstants.AUTH_HEAD) String authKey,
            HttpServletResponse http ) {
        if (!identityService.isAuthenticated(authKey)) {
            http.setStatus(401);
            return null;
        }

        return new SettingsView(identityService.getUser());
    }

    @CrossOrigin(origins = "*")
    @PutMapping(path = "/settings/")
    public boolean updateSettings(
            @RequestBody SettingsView settings,
            @RequestHeader(HeaderConstants.AUTH_HEAD) String authKey,
            HttpServletResponse http ) {
        if (!identityService.isAuthenticated(authKey)) {
            http.setStatus(401);
            return false;
        }

        boolean result = userService.updateSettings(settings);
        if (!result) {
            http.setStatus(400);
            return false;
        }

        return true;
    }
}
