package ru.job4j.dreamjob.service;

import ru.job4j.dreamjob.model.User;

import java.util.Optional;

/**
 * Интерфейс инкапсулирующий логику приложения
 *
 * @author Vasiliy Novopashin
 * @version 1.0
 * {@code @date} 11.03.2023
 */
public interface UserService {
    Optional<User> save(User user);

    Optional<User> findByEmailAndPassword(String email, String password);
}
