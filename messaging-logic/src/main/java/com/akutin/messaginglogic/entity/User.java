package com.akutin.messaginglogic.entity;


import javax.persistence.*;

import com.akutin.messaginglogic.common.utils.SecurityUtil;
import com.akutin.messaginglogic.common.views.SettingsView;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name="user")
@Getter
@Setter
public class User {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Integer id;

    @Column(name = "nickname", unique = true)
    private String nickname;

    @Column(name = "email", unique = true)
    private String email;

    @Column(name = "auth_key")
    private String authKey;

    @Column(name = "password_hash")
    private String passwordHash;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "address")
    private String address;

    @Column(name = "bio")
    private String bio;

    @Column(name = "phone")
    private String phone;

    @Column(name = "is_admin", columnDefinition = "boolean default false")
    private Boolean isAdmin = false;

    @ManyToMany(cascade = { CascadeType.PERSIST }, fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_thread",
            joinColumns = { @JoinColumn(name = "user_id") },
            inverseJoinColumns = { @JoinColumn(name = "thread_id") }
    )
    Set<Thread> threads = new HashSet<>();

    public void addThread(Thread thread) {
        this.threads.add(thread);
        thread.getUsers().add(this);
    }

    public void updateSettingsField(SettingsView view) {
        this.firstName = view.getFirstName();
        this.lastName = view.getLastName();
        this.address = view.getAddress();
        this.bio = view.getBio();
        this.phone = view.getPhone();

        this.email = view.getEmail();
        this.nickname = view.getNickname();
    }

    @ManyToMany(cascade = { CascadeType.PERSIST }, fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_friends",
            joinColumns = { @JoinColumn(name = "friend_one_id") },
            inverseJoinColumns = { @JoinColumn(name = "friend_two_id") }
    )
    Set<User> friends = new HashSet<>();

    public void addFriend(User user) {
        if (this.friends == null) {
            this.friends = new HashSet<>();
        }

        this.friends.add(user);
    }

    public void deleteFriend(User user) {
        if (this.friends == null) {
            return;
        }
        this.friends.remove(user);
    }

    public boolean hasFriend(Integer id) {
        if (this.friends == null) {
            return false;
        }

        for (User friend: friends) {
            if (friend.getId().equals(id)) {
                return true;
            }
        }

        return false;
    }

    public void setPassword(String password) {
        this.passwordHash = SecurityUtil.hash(password);
    }

    public boolean isAdmin() {
        return this.isAdmin;
    }

    public boolean hasPersonalWithTarget(Integer targetId) {
        if (threads == null || threads.size() == 0) {
            return false;
        }

        for (Thread thread: threads) {
            if (thread.isGroup()) {
                continue;
            }
            List<Integer> ids = thread.getUserIds();
            for (Integer id: ids) {
                if (id.equals(targetId)) {
                    return true;
                }
            }
        }

        return false;
    }
}