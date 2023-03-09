package ru.job4j.dreamjob.repository;

import net.jcip.annotations.ThreadSafe;
import org.springframework.stereotype.Repository;
import ru.job4j.dreamjob.model.Vacancy;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Класс реализует интерфейс VacancyRepository
 *
 * @author Vasiliy Novopashin
 * @version 1.0
 * {@code @date} 05.03.2023
 */

@ThreadSafe
@Repository
public class MemoryVacancyRepository implements VacancyRepository {

    private final AtomicInteger nextId = new AtomicInteger(0);

    private final Map<Integer, Vacancy> vacancies = new ConcurrentHashMap<>();

    private MemoryVacancyRepository() {
        save(new Vacancy(0, "Intern Java Developer", "Intern", LocalDateTime.now(), true, 1, 0));
        save(new Vacancy(0, "Junior Java Developer", "Junior", LocalDateTime.now(), true, 2, 0));
        save(new Vacancy(0, "Junior+ Java Developer", "Junior+", LocalDateTime.now(), false, 3, 0));
        save(new Vacancy(0, "Middle Java Developer", "Middle", LocalDateTime.now(), false, 1, 0));
        save(new Vacancy(0, "Middle+ Java Developer", "Middle+", LocalDateTime.now(), true, 2, 0));
        save(new Vacancy(0, "Senior Java Developer", "Senior", LocalDateTime.now(), false, 3, 0));
    }

    /**
     * Метод сохраняет вакансию в хранилище
     *
     * @param vacancy принимает вакансию
     * @return возвращает вакансию
     */
    @Override
    public Vacancy save(Vacancy vacancy) {
        vacancy.setId(nextId.incrementAndGet());
        vacancies.put(vacancy.getId(), vacancy);
        return vacancy;
    }

    /**
     * Метод удаляет вакансию по заданному id
     *
     * @param id вакансии, которую необходимо удалить
     * @return возвращает true в случае успешного удаления и false в противном случае
     */
    @Override
    public boolean deleteById(int id) {
        return vacancies.remove(id) != null;
    }

    /**
     * Метод обновляет вакансию
     *
     * @param vacancy принимает обновленную вакансию
     * @return возвращает true в случае успешного обновления и false в противном случае
     */
    @Override
    public boolean update(Vacancy vacancy) {
        return vacancies.computeIfPresent(vacancy.getId(), (id, oldVacancy) ->
                new Vacancy(oldVacancy.getId(),
                        vacancy.getTitle(),
                        vacancy.getDescription(),
                        vacancy.getCreationDate(),
                        vacancy.getVisible(),
                        vacancy.getCityId(),
                        vacancy.getFileId())) != null;
    }

    /**
     * Метод осуществляет поиск вакансии по id
     *
     * @param id идентификатор по которому осуществляется поиск
     * @return возвращает вакансию, обернутую в Optional<Vacancy>
     */
    @Override
    public Optional<Vacancy> findById(int id) {
        return Optional.ofNullable(vacancies.get(id));
    }

    /**
     * Метод возвращает список всех вакансий
     *
     * @return возвращает список всех вакансий
     */
    @Override
    public Collection<Vacancy> findAll() {
        return vacancies.values();
    }
}
