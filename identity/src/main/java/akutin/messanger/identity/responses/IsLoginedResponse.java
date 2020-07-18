package akutin.messanger.identity.responses;

import lombok.Getter;
import org.springframework.web.bind.annotation.ResponseBody;

@ResponseBody
@Getter
public class IsLoginedResponse {
    private boolean isLogined;

    public IsLoginedResponse(boolean isLogined) {
        this.isLogined = isLogined;
    }
}
