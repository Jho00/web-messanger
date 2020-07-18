package com.akutin.messaginglogic.common.views;

import com.akutin.messaginglogic.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class SettingsView {
    private Integer id;

    private String nickname;

    private String email;

    private String firstName;

    private String lastName;

    private String address;

    private String bio;

    private String phone;

    private boolean isFriended;

    private boolean sentRequest;

    private Boolean isAdmin;

    public SettingsView(User user) {
        this.nickname = user.getNickname();
        this.email = user.getEmail();
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
        this.address = user.getAddress();
        this.bio = user.getBio();
        this.phone = user.getPhone();
        this.id = user.getId();


        this.isFriended = false;
        this.sentRequest = false;
        this.isAdmin = user.isAdmin();
    }

    public SettingsView(User user, User me, boolean sentRequest) {
        this(user);

        this.isFriended = user.hasFriend(me.getId());
        this.sentRequest = sentRequest;
    }
}
