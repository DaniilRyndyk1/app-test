package com.javawhizz.App.controllers;

import com.javawhizz.App.models.User;
import com.javawhizz.App.services.AuthService;
import com.javawhizz.App.services.UserService;
import jakarta.security.auth.message.AuthException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController("api")
@Slf4j
@RequestMapping
@AllArgsConstructor
@CrossOrigin(origins = "*")
public class UserController {

    private final AuthService authService;
    private final UserService userService;

    @GetMapping("users")
    public List<User> getAll(@RequestParam String token) throws AuthException {
        if (authService.checkAccessToken(token)) {
            return userService.getAll();
        }
        throw new AuthException("Неверный токен");
    }
}

