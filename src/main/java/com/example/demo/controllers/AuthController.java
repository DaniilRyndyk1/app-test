package com.example.demo.controllers;

import jakarta.security.auth.message.AuthException;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import com.example.demo.exceptions.ValidationException;
import com.example.demo.jwt.JwtRequest;
import com.example.demo.jwt.JwtResponse;
import com.example.demo.models.User;
import com.example.demo.services.AuthService;
import com.example.demo.services.UserService;

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
