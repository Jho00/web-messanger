package com.akutin.messaginglogic.common.views;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

@Getter
@Setter
@ResponseBody
public class ThreadView {
    private Long lastMessageTime;
    private Integer lastActionId;
    private String threadName;
    private Integer id;
    private Iterable<MessageView> messages;
    private Iterable<UserView> users;
    private Map<Integer, Long> activities;
}
