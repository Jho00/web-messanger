package akutin.messanger.identity.services;

import akutin.messanger.identity.dalcontracts.UserRepository;
import akutin.messanger.identity.entity.User;
import akutin.messanger.identity.exceptions.UserNotFoundException;
import akutin.messanger.identity.responses.CreateUserResponse;
import org.springframework.stereotype.Service;

import java.security.InvalidParameterException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class IdentityStorageService {
    private final UserRepository userRepository;
    private TokenGenerator generator;

    private IdentityStorageService(UserRepository userRepository) {
        this.identities = new HashMap<>();
        this.generator = new TokenGenerator();
        this.userRepository = userRepository;
    }
    private Map<String, Integer> identities; // <AuthKey, UserID,>


    public boolean isLogined(String authKey) {
        return this.identities.containsKey(authKey);
    }

    public String login(String nickname, String password) throws UserNotFoundException, InvalidParameterException {
        Optional<User> user = this.userRepository.findUserByNickname(nickname);
        if (!user.isPresent()) {
            throw new UserNotFoundException("User not found");
        }

        String passwordHash = HashingService.hash(password);
        if (!user.get().getPasswordHash().equals(passwordHash)) {
            throw new InvalidParameterException("Invalid password");
        }

        if(this.isLogined(user.get().getAuthKey())) {
            this.logout(user.get().getAuthKey());
        }

        String token = this.generator.generateToken();
        user.get().setAuthKey(token);
        this.userRepository.save(user.get());
        this.identities.put(token, user.get().getId());

        return token;
    }

    public void logout(String authKey) {
        this.identities.remove(authKey);
    }

    public CreateUserResponse signup(String name, String email, String password, String repeatedPassword) throws InvalidParameterException {
        if (!password.equals(repeatedPassword)) {
            throw new InvalidParameterException("Пароль должны совпадать");
        }

        Optional<User> target = userRepository.findUserByNickname(name);
        if (target.isPresent()) {
            throw new InvalidParameterException("Такой пользователь уже существует");
        }
        String token = this.generator.generateToken();

        User user = new User();
        user.setEmail(email);
        user.setNickname(name);
        user.setAuthKey(token);
        user.setPasswordHash(HashingService.hash(password));

        user = this.userRepository.save(user);

        this.identities.put(token, user.getId());

        return new CreateUserResponse(token, user.getId());
    }
}
