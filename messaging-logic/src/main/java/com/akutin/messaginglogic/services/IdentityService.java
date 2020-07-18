package com.akutin.messaginglogic.services;

import com.akutin.messaginglogic.common.models.AuthModel;
import com.akutin.messaginglogic.common.views.UserView;
import com.akutin.messaginglogic.config.AppConfig;
import com.akutin.messaginglogic.config.AppConfigModel;
import com.akutin.messaginglogic.entity.User;
import com.akutin.messaginglogic.repository.UserRepository;
import lombok.Getter;
import lombok.SneakyThrows;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.WebApplicationContext;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
@Getter
@Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class IdentityService {
    private User user;
    private UserView userView = null;
    private AppConfigModel appConfig;
    private String identityUrl;

    private final UserRepository userRepository;

    private final RestTemplate restTemplate;

    @SneakyThrows
    public IdentityService(UserRepository userRepository, RestTemplateBuilder restTemplateBuilder) {
        this.userRepository = userRepository;
        this.restTemplate = restTemplateBuilder.build();

        appConfig = AppConfig.getConfig();
        identityUrl = appConfig.getIdentityHost() + ":";
        identityUrl += appConfig.getIdentityPort() + "/user?authKey=";
    }

    public boolean isAuthenticated(String authToken) {
        boolean isAuthenticated = this.getIsAuthenticated(authToken);
        if (!isAuthenticated) {
            return false;
        }

        Optional<User> user = userRepository.findByAuthKey(authToken);
        if (!user.isPresent()) {
            throw new RuntimeException("User not present");
        }
        this.user = user.get();
        return true;
    }

    public UserView getUserView() {
        if (this.userView == null) {
            UserView view = new UserView();
            view.setId(user.getId());
            view.setNickname(user.getNickname());
            view.setEmail(user.getEmail());

            this.userView = view;
        }

        return this.userView;
    }

    public Integer getUserId() {
        return this.getUser().getId();
    }

    private boolean getIsAuthenticated(String authToken) {
        AuthModel authModel = this.restTemplate.getForObject(this.identityUrl + authToken, AuthModel.class);
        if (authModel == null) {
            return false;
        }
        return authModel.logined;
    }

    public boolean logout() {
        return this.logout(this.getUser());
    }

    public boolean logout(User user) {
//        Map<String, String> params = new HashMap<>();
//        params.put("authKey", user.getAuthKey());
//
        restTemplate.delete(this.identityUrl + user.getAuthKey());
        return true;
    }
}
