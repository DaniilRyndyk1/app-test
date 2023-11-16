package com.javawhizz.App.controllers;

import com.javawhizz.App.exceptions.ValidationException;
import com.javawhizz.App.jwt.JwtRequest;
import com.javawhizz.App.jwt.JwtResponse;
import com.javawhizz.App.models.User;
import com.javawhizz.App.services.AuthService;
import com.javawhizz.App.services.UserService;
import jakarta.security.auth.message.AuthException;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@CrossOrigin(origins = "*")
public class AuthController {
    private final AuthService authService;
    private final UserService userService;

    @PostMapping("login")
    @CrossOrigin(origins = "*")
    public JwtResponse login(@RequestBody JwtRequest request) throws AuthException {
        return authService.login(request);
    }

    @PostMapping("refresh")
    public JwtResponse refresh(@RequestParam String token) throws AuthException {
        return authService.refresh(token);
    }

    @PostMapping("register")
    @ResponseStatus(HttpStatus.CREATED)
    public User register(@RequestBody User user) {
        validate(user);
        return userService.add(user);
    }

    private void validate(User user) {
        if (user.getLogin() == null) {
            throw new ValidationException("Логин не может отсутствовать");
        } else if (user.getLogin().isBlank()) {
            throw new ValidationException("Логин не может быть пустым");
        } else if (user.getLogin().contains(" ")) {
            throw new ValidationException("Логин не может содержать пробелы");
        }

        var sameUser = userService.getByLogin(user.getLogin());
        if (sameUser != null) {
            throw new ValidationException("Пользователь с таким логином уже существует");
        }
    }
}
