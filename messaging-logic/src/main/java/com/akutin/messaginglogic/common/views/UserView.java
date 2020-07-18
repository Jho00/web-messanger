package com.akutin.messaginglogic.common.views;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.bind.annotation.ResponseBody;

@ResponseBody
@Getter
@Setter
public class UserView {
    private String nickname;
    private String email;
    private Integer id;
}
