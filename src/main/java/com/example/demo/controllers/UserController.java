package com.example.demo.controllers;

import com.example.demo.exceptions.ValidationException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import com.example.demo.models.User;
import com.example.demo.services.AuthService;
import com.example.demo.services.UserService;

import java.util.List;

@RestController("api")
@Slf4j
@RequestMapping
@AllArgsConstructor
@CrossOrigin(origins = "*")
public class UserController{

    private final AuthService authService;
    private final UserService userService;

    @GetMapping("users")
    public List<User> getAll(@RequestParam String token) throws ValidationException {
        if (authService.checkAccessToken(token)) {
            return userService.getAll();
        }
        throw new ValidationException("Неверный токен");
    }
}

