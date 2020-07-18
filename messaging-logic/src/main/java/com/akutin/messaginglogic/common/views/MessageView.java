package com.akutin.messaginglogic.common.views;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@ResponseBody
@Getter
@Setter
public class MessageView {
    private Integer id;
    private Integer authorId;
    private String nickname;
    private String content;
    private Long datetime;
    private List<String> attachments;
}
