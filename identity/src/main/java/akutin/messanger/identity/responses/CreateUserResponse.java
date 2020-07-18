package akutin.messanger.identity.responses;

import lombok.Getter;
import org.springframework.web.bind.annotation.ResponseBody;

@ResponseBody
@Getter
public class CreateUserResponse {
    private String authToken;
    private int id;

    public CreateUserResponse(String authToken, int id) {
        this.authToken = authToken;
        this.id = id;
    }
}
