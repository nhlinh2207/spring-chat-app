package com.example.chatapp.api;

import com.example.chatapp.model.User;
import com.example.chatapp.service.IUserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@AllArgsConstructor
public class UserController {

    private final IUserService userService;

    @GetMapping(path = "/")
    public String login(){
        return "login";
    }

    @RequestMapping("/new-account")
    public String newAccount(Model model) {
        model.addAttribute("user", new User());
        return "new-account";
    }

    @PostMapping(path = "/new-account")
    public String createAccount(User user, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "new-account";
        }
        userService.createUser(user);
        return "redirect:/";
    }
}
