package com.akutin.messaginglogic.services;

import com.akutin.messaginglogic.common.exceptions.NotFoundInDbException;
import com.akutin.messaginglogic.common.requests.ThreadCreateModel;
import com.akutin.messaginglogic.common.utils.ClassCasterUtil;
import com.akutin.messaginglogic.common.views.ThreadView;
import com.akutin.messaginglogic.entity.Activity;
import com.akutin.messaginglogic.entity.Thread;
import com.akutin.messaginglogic.entity.User;
import com.akutin.messaginglogic.repository.ActivityRepository;
import com.akutin.messaginglogic.repository.MessageRepository;
import com.akutin.messaginglogic.repository.ThreadRepository;
import com.akutin.messaginglogic.repository.UserRepository;
import org.checkerframework.checker.nullness.Opt;
import org.springframework.stereotype.Service;
import java.util.*;

@Service
public class ThreadService {
    private final ThreadRepository threadRepository;
    private final IdentityService identityService;
    private final ActivityRepository activityRepository;
    private final UserRepository userRepository;
    private final WSService wsService;

    public ThreadService(ThreadRepository threadRepository,
                         IdentityService identityService,
                         ActivityRepository activityRepository,
                         UserRepository userRepository,
                         WSService wsService) {
        this.threadRepository = threadRepository;
        this.identityService = identityService;
        this.activityRepository = activityRepository;
        this.userRepository = userRepository;
        this.wsService = wsService;
    }

    public Iterable<ThreadView> getAllThreads() {
        HashSet<ThreadView> threadViews = new HashSet<>();

        Iterable<Thread> threads = identityService.getUser().getThreads();
        threads.forEach((item -> {
            threadViews.add(ClassCasterUtil.castThreadToThreadView(item, identityService.getUserId()));
        }));
        return threadViews;
    }

    public ThreadView getThread(Integer threadId) throws NotFoundInDbException {
        Optional<Thread> thread = threadRepository.findById(threadId);
        if (!thread.isPresent()) {
            throw new NotFoundInDbException("Thread not found");
        }

        return ClassCasterUtil.castThreadToThreadView(thread.get(), identityService.getUserId());
    }

    public boolean create(ThreadCreateModel model) {
        if (model.users.size() == 1) {
            Integer id = model.users.get(0);
            if (identityService.getUser().hasPersonalWithTarget(id)) {
                return true;
            }
        }

        
        Thread thread = new Thread();
        thread.setIsGroup(model.users.size() > 1);
        thread.setName(model.name);
        thread.setOwnerId(identityService.getUserId());

        Long time = System.currentTimeMillis();
        thread.setCreatedTime(time);
        thread.setUpdatedTime(time);
        thread.setLastActionId(identityService.getUserId());
        Activity activity = thread.updateActivity(identityService.getUserId(), time);
        thread = threadRepository.save(thread);

        Iterable<User> users = userRepository.findUsersByIdIn(model.users);
        List<Integer> finalIds = new ArrayList<>();
        finalIds.add(identityService.getUserId());

        Thread finalThread = thread;
        users.forEach(item -> {
            item.addThread(finalThread);
            finalIds.add(item.getId());
        });

        identityService.getUser().addThread(thread);

        userRepository.saveAll(users);
        userRepository.save(identityService.getUser());
        activityRepository.save(activity);

        try {
            wsService.notifyWsNewThread(finalIds);
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return true;
    }


    public boolean makeSeenThread(Integer threadId) {
        Optional<Thread> optionalThread = threadRepository.findById(threadId);
        if (!optionalThread.isPresent()) {
            return false;
        }

        Thread thread = optionalThread.get();
        Activity activity = thread.updateActivity(identityService.getUserId(), System.currentTimeMillis());

        activityRepository.save(activity);
        threadRepository.save(thread);

        return true;
    }
}
