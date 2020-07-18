package akutin.messanger.identity.responses;


import lombok.Getter;
import org.springframework.web.bind.annotation.ResponseBody;

@ResponseBody
@Getter
public class LogoutResponse {
    private String status = "ok";
}
