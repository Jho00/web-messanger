package akutin.messanger.identity.dalcontracts;

import akutin.messanger.identity.entity.User;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface UserRepository extends CrudRepository<User, Integer> {
    public Optional<User> findUserByNickname(String nickname);
}
