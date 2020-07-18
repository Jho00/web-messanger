package com.akutin.messaginglogic.services;

import com.akutin.messaginglogic.common.exceptions.NotFoundInDbException;
import com.akutin.messaginglogic.common.utils.ClassCasterUtil;
import com.akutin.messaginglogic.common.utils.SecurityUtil;
import com.akutin.messaginglogic.common.views.SettingsView;
import com.akutin.messaginglogic.common.views.UserView;
import com.akutin.messaginglogic.entity.FriendRequest;
import com.akutin.messaginglogic.entity.User;
import com.akutin.messaginglogic.repository.FriendRequestRepository;
import com.akutin.messaginglogic.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final FriendRequestRepository friendRequestRepository;

    private final IdentityService identityService;
    private final WSService wsService;

    private final EmailService emailService;

    public UserService(UserRepository userRepository, FriendRequestRepository friendRequestRepository, IdentityService identityService, WSService wsService, EmailService emailService) {
        this.userRepository = userRepository;
        this.friendRequestRepository = friendRequestRepository;
        this.identityService = identityService;
        this.wsService = wsService;
        this.emailService = emailService;
    }

    public Iterable<UserView> find(String partName) {
        Iterable<User> foundUsers = userRepository.findAllByNicknameContaining(partName);

        Set<UserView> views = new HashSet<>();
        foundUsers.forEach(item -> {
            views.add(ClassCasterUtil.castUserToUserView(item));
        });

        return views;
    }

    @Transactional
    public boolean updateSettings(SettingsView view) {
        User user = identityService.getUser();

        Integer id = identityService.getUserId();
        List<User> users = userRepository.findAllByNicknameOrEmail(view.getNickname(), view.getEmail());

        try {
            users.forEach(item -> {
                if (!item.getId().equals(id)) {
                    throw new RuntimeException();
                }
            });
        } catch (RuntimeException e) {
            return false;
        }

        user.updateSettingsField(view);
        userRepository.save(user);

        return true;
    }

    public User getProfile(Integer id, String nickname) throws NotFoundInDbException {
        Optional<User> user = userRepository.findByIdOrNickname(id, nickname);

        if (!user.isPresent()) {
            throw new NotFoundInDbException();
        }

        return user.get();
    }

    public boolean addFriend(Integer id) {
        Optional<User> target = userRepository.findById(id);
        if (!target.isPresent()) {
            return false;
        }

        Optional<FriendRequest> friendRequest = friendRequestRepository.findByTargetIdAndOwnerId(identityService.getUserId(), target.get().getId());

        if (friendRequest.isPresent()) {
            this.acceptExistedRequest(friendRequest.get());
            return true;
        }

        FriendRequest request = new FriendRequest();
        request.setOwnerId(identityService.getUserId());
        request.setTargetId(target.get().getId());
        request.setTargetNickname(target.get().getNickname());
        request.setOwnerNickname(identityService.getUser().getNickname());

        this.friendRequestRepository.save(request);
        this.notifyRequest(request.getTargetId(), request.getOwnerId());
        return true;
    }

    public boolean deleteFriend(Integer id) {
        Optional<User> target = userRepository.findById(id);
        if (!target.isPresent()) {
            return false;
        }

        User user = target.get();
        user.deleteFriend(identityService.getUser());
        identityService.getUser().deleteFriend(user);

        userRepository.save(user);
        userRepository.save(identityService.getUser());
        this.notifyRequest(user.getId(), identityService.getUserId());
        return true;
    }


    public Iterable<FriendRequest> getFriendRequests() {
        return friendRequestRepository.findAllByTargetId(identityService.getUserId());
    }

    public boolean acceptRequest(Integer requestId) {
        Optional<FriendRequest> friendRequestOptional = friendRequestRepository.findById(requestId);
        if (!friendRequestOptional.isPresent()) {
            System.out.println("Request is not present"); // TODO: implement logger
            return false;
        }

        FriendRequest request = friendRequestOptional.get();

        return this.acceptExistedRequest(request);
    }

    public boolean declineRequest(Integer requestId) {
        friendRequestRepository.deleteById(requestId);
        return true;
    }

    @Transactional
    protected boolean acceptExistedRequest(FriendRequest request) {
        Optional<User> targetOptional = userRepository.findById(request.getTargetId());
        Optional<User> ownerOptional = userRepository.findById(request.getOwnerId());

        if (!targetOptional.isPresent() || !ownerOptional.isPresent()) {
            System.out.println("Some user is not present"); // TODO: implement logger
            return false;
        }

        User target = targetOptional.get();
        User owner = ownerOptional.get();

        owner.addFriend(target);
        target.addFriend(owner);

        userRepository.save(owner);
        userRepository.save(target);
        friendRequestRepository.deleteById(request.getId());

        this.notifyRequest(owner.getId(), target.getId());
        return true;
    }

    private void notifyRequest(Integer id1, Integer id2) {
        List<Integer> ids = new ArrayList<>();
        ids.add(id1);
        ids.add(id2);
        try {
            wsService.notifyWsNewRequest(ids);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean resetPassword(Integer id) {
        Optional<User> optionalUser = userRepository.findById(id);
        if (!optionalUser.isPresent()) {
            return false;
        }

        User user = optionalUser.get();
        String newPassword = SecurityUtil.randomPassword(12);
        user.setPassword(newPassword);
        userRepository.save(user);

        emailService.sendMessage("Новый пароль", "Ваш пароль: " + newPassword, user);
        identityService.logout(user);
        return true;
    }
}
