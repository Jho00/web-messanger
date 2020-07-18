package com.akutin.messaginglogic.common.wsrequests;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter @Setter
public class NewMessageRequest {
    private List<Integer> ids = new ArrayList<>();
    private NewMessageData message;
}
