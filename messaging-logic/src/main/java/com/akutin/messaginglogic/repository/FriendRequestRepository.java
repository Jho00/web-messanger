package com.akutin.messaginglogic.repository;

import com.akutin.messaginglogic.entity.FriendRequest;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface FriendRequestRepository extends CrudRepository<FriendRequest, Integer> {
    Iterable<FriendRequest> findAllByTargetId(Integer id);
    Optional<FriendRequest> findByTargetIdAndOwnerId(Integer target, Integer owner);
}
