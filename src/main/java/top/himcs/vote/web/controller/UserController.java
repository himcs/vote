package top.himcs.vote.web.controller;

import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import top.himcs.vote.model.User;

@RestController
public class UserController {

    @GetMapping("/user")
    @JsonView({User.WithoutPasswordView.class})
    public User getUser() {
        throw new RuntimeException("good");
//        return new User("eric", "7!jd#h23");
    }

    @GetMapping("/current")
    public String current(Authentication authentication) {

        if (authentication == null) {
            return "尚未登陆";
        }
        return authentication.getPrincipal().toString();
    }

}
