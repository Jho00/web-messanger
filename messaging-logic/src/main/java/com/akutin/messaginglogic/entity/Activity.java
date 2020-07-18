package com.akutin.messaginglogic.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name="activity")
@Getter
@Setter
public class Activity {
    @Id
    @Column(name="id")
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Integer id;

    @Column(name="user_id")
    private Integer userId;

    @Column(name="activity_time")
    private Long activityTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="thread_id", referencedColumnName = "id")
    private Thread baseThread;
}
