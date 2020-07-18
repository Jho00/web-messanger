package com.akutin.messaginglogic.common.wsrequests;


import com.akutin.messaginglogic.entity.Message;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class NewMessageData {
    private Integer authorId;
    private String nickname;
    private String content;
    private Long datetime;
    private Integer threadId;
    private List<String> attachments = new ArrayList<>();

    public void build(Message entity) {
        this.authorId = entity.getOwnerId();
        this.nickname = entity.getOwnerNickname();
        this.content = entity.getContent();
        this.datetime = entity.getMessageTime();
        this.threadId = entity.getThread().getId();
        entity.getAttachments().forEach(item -> {
            this.attachments.add(item.getUrl());
        });
    }
}
