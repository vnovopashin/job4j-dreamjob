package ru.job4j.dreamjob.repository;

import net.jcip.annotations.ThreadSafe;
import org.springframework.stereotype.Repository;
import ru.job4j.dreamjob.model.Candidate;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Класс реализует интерфейс CandidateRepository
 *
 * @author Vasiliy Novopashin
 * @version 1.0
 * {@code @date} 05.03.2023
 */

@ThreadSafe
@Repository
public class MemoryCandidateRepository implements CandidateRepository {

    private final AtomicInteger nextId = new AtomicInteger(0);

    private final Map<Integer, Candidate> candidates = new ConcurrentHashMap<>();

    private MemoryCandidateRepository() {
        save(new Candidate(0, "De vito Denny", "Intern Java Developer", LocalDateTime.now(), 0));
        save(new Candidate(0, "Ivanov Ivan", "Junior Java Developer", LocalDateTime.now(), 0));
        save(new Candidate(0, "Petrov Petr", "Junior+ Java Developer", LocalDateTime.now(), 0));
        save(new Candidate(0, "Chernoff Sergey", "Middle Java Developer", LocalDateTime.now(), 0));
        save(new Candidate(0, "Smirnoff Andrey", "Middle+ Java Developer", LocalDateTime.now(), 0));
        save(new Candidate(0, "Novikov Nikolay", "Senior Java Developer", LocalDateTime.now(), 0));
    }

    /**
     * Метод сохраняет кандидата в хранилище
     *
     * @param candidate принимает кандидата
     * @return возвращает кандидата
     */
    @Override
    public Candidate save(Candidate candidate) {
        candidate.setId(nextId.incrementAndGet());
        candidates.put(candidate.getId(), candidate);
        return candidate;
    }

    /**
     * Метод удаляет кандидата по заданному id
     *
     * @param id кандидата, которого необходимо удалить
     * @return возвращает true в случае успешного удаления и false в противном случае
     */
    @Override
    public boolean deleteById(int id) {
        return candidates.remove(id) != null;
    }

    /**
     * Метод обновляет кандидата
     *
     * @param candidate принимает обновленного кандидата
     * @return возвращает true в случае успешного обновления и false в противном случае
     */
    @Override
    public boolean update(Candidate candidate) {
        return candidates.computeIfPresent(candidate.getId(), (id, oldCandidate) ->
                new Candidate(oldCandidate.getId(),
                        candidate.getName(),
                        candidate.getDescription(),
                        candidate.getCreationDate(),
                        candidate.getFileId())) != null;
    }

    /**
     * Метод осуществляет поиск кандидата по id
     *
     * @param id идентификатор по которому осуществляется поиск
     * @return возвращает кандидата, обернутого в Optional<Candidate>
     */
    @Override
    public Optional<Candidate> findById(int id) {
        return Optional.ofNullable(candidates.get(id));
    }

    /**
     * Метод возвращает список всех кандидатов
     *
     * @return возвращает список всех кандидатов
     */
    @Override
    public Collection<Candidate> findAll() {
        return candidates.values();
    }
}
