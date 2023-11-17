package com.example.demo.services;

import com.example.demo.exceptions.ValidationException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import com.example.demo.models.User;
import com.example.demo.storages.UserStorage;

import java.util.List;

@Service
@AllArgsConstructor
public class UserService {

    private final UserStorage storage;

    public List<User> getAll() {
        return storage.getAll();
    }

    public User getByLogin(String login) {
        return storage.getByLogin(login);
//        if (user == null) {
//            throw new ValidationException("Пользователь " + login + "не найден");
//        }
//        return user;
    }

    public User add(User user) {
        return storage.add(user);
    }
}