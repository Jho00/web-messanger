package com.akutin.messaginglogic.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.*;

@Entity
@Table(name="thread")
@Getter
@Setter
public class Thread {
    @Id
    @Column(name="id")
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Integer id;

    @Column(name="owner_id")
    private Integer ownerId;

    @Column(name="name")
    private String name;

    @Column(name="is_group")
    private Boolean isGroup;

    @Column(name="created_time")
    private Long createdTime;

    @Column(name="updated_time")
    private Long updatedTime;

    @Column(name="last_action_id")
    private Integer lastActionId;

    @ManyToMany(mappedBy = "threads", cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
    Set<User> users = new HashSet<>();

    public boolean isGroup() {
        return this.isGroup;
    }

    public List<Integer> getUserIds() {
        List<Integer> ids = new ArrayList<>();
        this.users.forEach(item -> {
            ids.add(item.getId());
        });
        return ids;
    }

    @OneToMany(
            mappedBy = "thread",
            cascade = CascadeType.PERSIST,
            fetch = FetchType.LAZY)
    private Set<Message> messages;

    @OneToMany(
            mappedBy = "baseThread",
            cascade = CascadeType.PERSIST,
            fetch = FetchType.LAZY)
    private Set<Activity> activities;

    public Activity updateActivity(Integer userId, Long activityTime) {
        if (activities == null) {
            this.activities = new HashSet<>();
        }

        Activity activity = null;

        for (Activity a : activities) {
            Integer activityUserId = a.getUserId();
            if (userId.equals(activityUserId)) {
                activity = a;
                break;
            }
        }

        if (activity == null) {
            activity = new Activity();
            activity.setUserId(userId);
        }

        activity.setActivityTime(activityTime);
        activity.setBaseThread(this);
        this.activities.add(activity);
        return activity;
    }
}
