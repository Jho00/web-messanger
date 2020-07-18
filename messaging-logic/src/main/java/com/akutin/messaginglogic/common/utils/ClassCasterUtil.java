package com.akutin.messaginglogic.common.utils;

import com.akutin.messaginglogic.common.views.MessageView;
import com.akutin.messaginglogic.common.views.ThreadView;
import com.akutin.messaginglogic.common.views.UserView;
import com.akutin.messaginglogic.entity.Message;
import com.akutin.messaginglogic.entity.Thread;
import com.akutin.messaginglogic.entity.User;

import java.util.*;

public class ClassCasterUtil {
    private ClassCasterUtil() {}

    public static UserView castUserToUserView(User user) {
        UserView view = new UserView();
        view.setNickname(user.getNickname());
        view.setId(user.getId());
        view.setEmail(user.getEmail());
        return view;
    }

    public static MessageView castMessageToMessageView(Message message) {
        MessageView view = new MessageView();
        view.setAuthorId(message.getOwnerId());
        view.setContent(message.getContent());
        view.setDatetime(message.getMessageTime());
        view.setNickname(message.getOwnerNickname());
        view.setId(message.getId());

        List<String> urls = new ArrayList<>();
        message.getAttachments().forEach(item -> {
            urls.add(item.getUrl());
        });
        view.setAttachments(urls);
        return view;
    }

    public static ThreadView castThreadToThreadView(Thread thread, Integer myId) {
        ThreadView view = new ThreadView();
        view.setId(thread.getId());
        view.setLastMessageTime(thread.getUpdatedTime());
        view.setLastActionId(thread.getLastActionId());
        view.setThreadName(thread.getName());

        Iterable<Message> messages = thread.getMessages();
        Set<MessageView> messageViews = new HashSet<>();
        messages.forEach((item -> {
            messageViews.add(ClassCasterUtil.castMessageToMessageView(item));
        }));
        view.setMessages(messageViews);

        Iterable<User> users = thread.getUsers();
        Set<UserView> usersView = new HashSet<>();
        users.forEach((item -> {
            if (item.getId().equals(myId)) {
                return;
            }
            usersView.add(ClassCasterUtil.castUserToUserView(item));
        }));
        view.setUsers(usersView);

        Map<Integer, Long> activities = new HashMap<>();
        thread.getActivities().forEach(item -> {
            activities.put(item.getUserId(), item.getActivityTime());
        });
        view.setActivities(activities);

        return view;
    }
}
