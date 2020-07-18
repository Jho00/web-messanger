package com.akutin.messaginglogic.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name="attachment")
@Getter
@Setter
public class Attachment {
    @Id
    @Column(name="id")
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Integer id;

    @Column(name="url")
    private String url;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="message_id", referencedColumnName = "id")
    private Message message;
}
