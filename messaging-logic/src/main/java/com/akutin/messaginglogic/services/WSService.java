package com.akutin.messaginglogic.services;

import com.akutin.messaginglogic.common.wsrequests.NewMessageData;
import com.akutin.messaginglogic.common.wsrequests.NewMessageRequest;
import com.akutin.messaginglogic.config.AppConfig;
import com.akutin.messaginglogic.config.AppConfigModel;
import com.akutin.messaginglogic.entity.Message;
import lombok.SneakyThrows;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class WSService {
    private final static String ROUTE_NEW_MESSAGE =  "/new-message";
    private final static String ROUTE_NEW_THREAD =  "/new-thread";
    private final static String ROUTE_NEW_REQUEST =  "/new-request";

    private AppConfigModel appConfig;
    private final RestTemplate restTemplate;

    private String url;

    @SneakyThrows
    public WSService(RestTemplateBuilder restTemplateBuilder) {
        appConfig = AppConfig.getConfig();
        this.restTemplate = restTemplateBuilder.build();

        this.url = appConfig.getWsHost() + ":" + appConfig.getWsPort();
    }

    public void notifyWSNewMessage(List<Integer> ids, Message message) {
        NewMessageData data = new NewMessageData();
        data.build(message);

        NewMessageRequest request = new NewMessageRequest();
        request.setMessage(data);
        request.setIds(ids);

        String target = url + ROUTE_NEW_MESSAGE;
        System.out.println("TARGET URL: " + target); // TODO: implement logger

        this.restTemplate.postForLocation(target, request);
    }

    public void notifyWsNewThread(List<Integer> ids) {
        NewMessageRequest request = new NewMessageRequest();
        request.setIds(ids);

        String target = url + ROUTE_NEW_THREAD;
        System.out.println("TARGET URL: " + target); // TODO: implement logger

        this.restTemplate.postForLocation(target, request);
    }

    public void notifyWsNewRequest(List<Integer> ids) {
        NewMessageRequest request = new NewMessageRequest();
        request.setIds(ids);

        String target = url + ROUTE_NEW_REQUEST;
        System.out.println("TARGET URL: " + target); // TODO: implement logger

        this.restTemplate.postForLocation(target, request);
    }
}
