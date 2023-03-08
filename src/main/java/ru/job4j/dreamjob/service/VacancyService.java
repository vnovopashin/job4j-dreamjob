package ru.job4j.dreamjob.service;

import ru.job4j.dreamjob.model.Vacancy;

import java.util.Collection;
import java.util.Optional;

/**
 * Интерфейс инкапсулирующий логику приложения
 *
 * @author Vasiliy Novopashin
 * @version 1.0
 * {@code @date} 08.03.2023
 */
public interface VacancyService {
    Vacancy save(Vacancy vacancy);

    boolean deleteById(int id);

    boolean update(Vacancy vacancy);

    Optional<Vacancy> findById(int id);

    Collection<Vacancy> findAll();
}
