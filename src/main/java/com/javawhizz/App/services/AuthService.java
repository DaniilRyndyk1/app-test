package com.javawhizz.App.services;

import com.javawhizz.App.jwt.JwtProvider;
import com.javawhizz.App.jwt.JwtRequest;
import com.javawhizz.App.jwt.JwtResponse;
import com.javawhizz.App.models.User;
import jakarta.security.auth.message.AuthException;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final Map<String, String> refreshTokens = new HashMap<>();

    private final UserService userService;

    private final JwtProvider jwtProvider;

    public JwtResponse login(@NonNull JwtRequest request) throws AuthException {
        var login = request.getLogin();
        var user = userService.getByLogin(login);

        if (user == null) {
            throw new AuthException("Пользователь не найден");
        }

        var password = request.getPassword();

        if (user.getPassword().equals(password)) {
            return createNewTokens(user);
        } else {
            throw new AuthException("Неправильный пароль");
        }
    }

    public JwtResponse refresh(@NonNull String refreshToken) throws AuthException {
        var isTokenCorrect = jwtProvider.validateRefreshToken(refreshToken);
        if (isTokenCorrect) {
            var login = jwtProvider.getRefreshClaims(refreshToken).getSubject();
            var saveRefreshToken = refreshTokens.get(login);

            if (saveRefreshToken != null && saveRefreshToken.equals(refreshToken)) {
                var user = userService.getByLogin(login);

                if (user == null) {
                    throw new AuthException("Пользователь не найден");
                }

                return createNewTokens(user);
            }
        }
        throw new AuthException("Невалидный JWT токен");
    }

    public boolean checkAccessToken(String token) {
        return jwtProvider.validateAccessToken(token);
    }

    private JwtResponse createNewTokens(User user) {
        final String accessToken = jwtProvider.generateAccessToken(user);
        final String refreshToken = jwtProvider.generateRefreshToken(user);
        refreshTokens.put(user.getLogin(), refreshToken);
        return new JwtResponse(accessToken, refreshToken);
    }
}
