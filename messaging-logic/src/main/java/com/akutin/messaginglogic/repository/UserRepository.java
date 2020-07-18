package com.akutin.messaginglogic.repository;

import com.akutin.messaginglogic.entity.User;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends CrudRepository<User, Integer> {
    Iterable<User> findUsersByIdIn(List<Integer> id);
    Iterable<User> findAllByNicknameContaining(String namePart);

    List<User> findAllByNicknameOrEmail(String nickname, String email);

    Optional<User> findByAuthKey(String key);
    Optional<User> findByIdOrNickname(Integer id, String nickname);
}
