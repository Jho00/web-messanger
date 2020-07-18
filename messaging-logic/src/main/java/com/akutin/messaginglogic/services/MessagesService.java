package com.akutin.messaginglogic.services;

import com.akutin.messaginglogic.common.requests.SendMessageModel;
import com.akutin.messaginglogic.entity.Activity;
import com.akutin.messaginglogic.entity.Message;
import com.akutin.messaginglogic.entity.Thread;
import com.akutin.messaginglogic.repository.ActivityRepository;
import com.akutin.messaginglogic.repository.MessageRepository;
import com.akutin.messaginglogic.repository.ThreadRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class MessagesService {
    private final IdentityService identityService;
    private final WSService wsService;

    private final MessageRepository messageRepository;
    private final ThreadRepository threadRepository;
    private final ActivityRepository activityRepository;

    public MessagesService(IdentityService identityService,
                           WSService wsService,
                           MessageRepository messageRepository,
                           ThreadRepository threadRepository,
                           ActivityRepository activityRepository) {
        this.identityService = identityService;
        this.wsService = wsService;

        this.messageRepository = messageRepository;
        this.threadRepository = threadRepository;
        this.activityRepository = activityRepository;
    }

    public void sendMessage(SendMessageModel model) {
        Optional<Thread> threadOptional = threadRepository.findById(model.threadId);

        if (!threadOptional.isPresent()) {
            throw new RuntimeException("Cannot find thread");
        }

        Long time = System.currentTimeMillis();

        Message message = new Message();
        message.setContent(model.content);
        message.setMessageTime(time);
        message.setOwnerId(identityService.getUserId());
        message.setOwnerNickname(identityService.getUser().getNickname());
        message.addAttachments(model.urls);
        message = messageRepository.save(message);

        Thread thread = threadOptional.get();

        thread.getMessages().add(message);
        thread.setUpdatedTime(time);
        thread.setLastActionId(identityService.getUserId());
        message.setThread(thread);

        Activity activity = thread.updateActivity(identityService.getUserId(), time);
        if (activity != null) {
            activityRepository.save(activity);
        }

        threadRepository.save(thread);
        messageRepository.save(message);

        try {
            wsService.notifyWSNewMessage(thread.getUserIds(), message);
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }
}
