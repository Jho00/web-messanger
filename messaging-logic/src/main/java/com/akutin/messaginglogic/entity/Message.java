package com.akutin.messaginglogic.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name="message")
@Getter
@Setter
public class Message {
    @Id
    @Column(name="id")
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Integer id;

    @Lob
    @Column(name="content", length=60000)
    private String content;

    @Column(name="owner_id")
    private Integer ownerId;

    @Column(name="owner_nickname")
    private String ownerNickname;

    @Column(name="message_time")
    private Long messageTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="thread_id", referencedColumnName = "id")
    private Thread thread;

    @OneToMany(
            mappedBy = "message",
            cascade = CascadeType.PERSIST,
            fetch = FetchType.LAZY)
    private Set<Attachment> attachments;

    public void addAttachments(List<String> urls) {
        if (this.attachments == null) {
            this.attachments = new HashSet<>();
        }

        urls.forEach(item -> {
            Attachment attachment = new Attachment();
            attachment.setUrl(item);
            attachment.setMessage(this);
            this.attachments.add(attachment);
        });
    }
}
