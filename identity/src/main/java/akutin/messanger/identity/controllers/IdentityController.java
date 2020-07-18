package akutin.messanger.identity.controllers;

import akutin.messanger.identity.dalcontracts.UserRepository;
import akutin.messanger.identity.exceptions.UserNotFoundException;
import akutin.messanger.identity.responses.CreateUserResponse;
import akutin.messanger.identity.responses.IsLoginedResponse;

import akutin.messanger.identity.responses.LoginResponse;
import akutin.messanger.identity.responses.LogoutResponse;
import akutin.messanger.identity.services.IdentityStorageService;
import akutin.messanger.identity.viewmodels.CreateUserViewModel;
import akutin.messanger.identity.viewmodels.LoginViewModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.security.InvalidParameterException;

@RestController
public class IdentityController {
    private final IdentityStorageService identityStorageService;

    public IdentityController(IdentityStorageService identityStorageService) {
        this.identityStorageService = identityStorageService;
    }

    @GetMapping(path = "/user")
    @CrossOrigin(origins="*")
    public @ResponseBody
    IsLoginedResponse isLogined(HttpServletResponse response, @RequestParam(required = true) String authKey) {
        boolean isLogined = identityStorageService.isLogined(authKey);
        return new IsLoginedResponse(isLogined);
    }

    @PutMapping(path = "/user")
    @CrossOrigin(origins="*")
    public @ResponseBody
    LoginResponse login(HttpServletResponse response, @RequestBody LoginViewModel loginViewModel) {
        String token = null;
        try {
            token = identityStorageService.login(loginViewModel.nickname, loginViewModel.password);
        } catch (UserNotFoundException e) {
            e.printStackTrace();
            response.setStatus(404);
            return new LoginResponse("Error");
        } catch (InvalidParameterException e) {
            e.printStackTrace();
            response.setStatus(400);
            return new LoginResponse("Invalid password");
        }
        return new LoginResponse(token);
    }

    @PostMapping(path = "/user")
    @CrossOrigin(origins="*")
    public @ResponseBody
    CreateUserResponse createUser(HttpServletResponse response, @RequestBody CreateUserViewModel createUserViewModel) {
        try {
            return identityStorageService.signup(
                    createUserViewModel.getName(),
                    createUserViewModel.getEmail(),
                    createUserViewModel.getPassword(),
                    createUserViewModel.getRepeatedPassword());
        } catch (InvalidParameterException e) {
            response.setStatus(400);
            return new CreateUserResponse(e.getMessage(), -1);
        }
    }

    @DeleteMapping(path = "/user")
    @CrossOrigin(origins="*")
    public @ResponseBody
    LogoutResponse logout(HttpServletResponse response, @RequestParam String authKey) {
        identityStorageService.logout(authKey);
        return new LogoutResponse();
    }
}
