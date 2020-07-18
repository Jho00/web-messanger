package akutin.messanger.identity.viewmodels;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateUserViewModel {
    private String name;
    private String email;
    private String password;
    private String repeatedPassword;
}
