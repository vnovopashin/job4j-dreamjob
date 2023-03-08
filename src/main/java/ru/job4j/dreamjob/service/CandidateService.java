package ru.job4j.dreamjob.service;

import ru.job4j.dreamjob.model.Candidate;

import java.util.Collection;
import java.util.Optional;

/**
 * Интерфейс инкапсулирующий логику приложения
 *
 * @author Vasiliy Novopashin
 * @version 1.0
 * {@code @date} 08.03.2023
 */
public interface CandidateService {
    Candidate save(Candidate candidate);

    boolean deleteById(int id);

    boolean update(Candidate candidate);

    Optional<Candidate> findById(int id);

    Collection<Candidate> findAll();
}
