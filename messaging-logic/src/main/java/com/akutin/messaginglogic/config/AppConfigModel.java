package com.akutin.messaginglogic.config;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AppConfigModel {
    private String identityHost;
    private String identityPort;

    private String wsHost;
    private String wsPort;
}
