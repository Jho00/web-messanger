package akutin.messanger.identity.services;

import java.util.UUID;

public class TokenGenerator {

    public String generateToken() {
        return UUID.randomUUID().toString();
    }
}
