package com.akutin.messaginglogic.repository;

import com.akutin.messaginglogic.entity.Thread;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ThreadRepository extends CrudRepository<Thread, Integer> {
    public Iterable<Thread> findAllByOwnerId(Integer ownerId);
    public Iterable<Thread> findAllByIdIn(List<Integer> ids);
}
