package akutin.messanger.identity.responses;

import lombok.Getter;
import org.springframework.web.bind.annotation.ResponseBody;

@ResponseBody
@Getter
public class LoginResponse {
    private String authToken;

    public LoginResponse(String authToken) {
        this.authToken = authToken;
    }
}
