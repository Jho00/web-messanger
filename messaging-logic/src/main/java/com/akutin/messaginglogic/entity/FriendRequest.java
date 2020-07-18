package com.akutin.messaginglogic.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name="friend_requests")
@Getter
@Setter
public class FriendRequest {
    @Id
    @Column(name="id")
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Integer id;

    @Column(name="target_id")
    private Integer targetId;

    @Column(name="owner_id")
    private Integer ownerId;

    @Column(name = "owner_nickname")
    private String ownerNickname;

    @Column(name = "target_nickname")
    private String targetNickname;
}
