package com.akutin.messaginglogic.repository;

import com.akutin.messaginglogic.entity.Message;

import org.springframework.data.repository.CrudRepository;


public interface MessageRepository extends CrudRepository<Message, Integer> {
    public Iterable<Message> findAllByThreadId(Integer threadId);

}
