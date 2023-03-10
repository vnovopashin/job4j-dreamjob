package ru.job4j.dreamjob.repository;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.job4j.dreamjob.configuration.DatasourceConfiguration;
import ru.job4j.dreamjob.model.File;
import ru.job4j.dreamjob.model.Vacancy;

import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Properties;

import static java.time.LocalDateTime.now;
import static java.util.Collections.emptyList;
import static java.util.Optional.empty;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

/**
 * Класс тестирует методы класса Sql2oVacancyRepository
 *
 * @author Vasiliy Novopashin
 * @version 1.0
 * {@code @date} 10.03.2023
 */
class Sql2oVacancyRepositoryTest {
    /**
     * Тестируем мы Sql2oVacancyRepository, поэтому нам нужно создать объект этого класса.
     * При этом создавать и настраивать каждый раз не имеет смысла,
     * поэтому создаем один экземпляр на весь тестовый класс, т.е. делаем статическим.
     */
    private static Sql2oVacancyRepository sql2oVacancyRepository;

    /**
     * Модель Vacancy зависит от City и File. Города создаются в отдельном скрипте, а файлы нет.
     * При сохранении вакансии мы указываем файл, поэтому нам нужно этот файл создать,
     * иначе будет нарушено ограничение внешнего ключа при сохранении Vacancy.
     * Для сохранения файла нужно создать репозиторий для него. Будем ссылаться на один файл во всех вакансиях,
     * поэтому его делаем статическим
     */
    private static Sql2oFileRepository sql2oFileRepository;

    private static File file;

    /**
     * Читаем настройки к тестовой БД из файла connection.properties
     *
     * @throws Exception может выбросить исключение
     */
    @BeforeAll
    public static void initRepositories() throws Exception {
        var properties = new Properties();
        try (var inputStream = Sql2oVacancyRepositoryTest.class.getClassLoader().getResourceAsStream("connection.properties")) {
            properties.load(inputStream);
        }
        var url = properties.getProperty("datasource.url");
        var username = properties.getProperty("datasource.username");
        var password = properties.getProperty("datasource.password");

        /**
         *Прежде чем создавать репозитории нам нужно создать клиент БД Sql2o. Он в свою очередь зависит от пула соединений.
         *  До этого за нас все настраивал Spring.
         *  Теперь нам нужно это сделать "руками".
         *  Т.е. вызвать метод connectionPool() для создания пула соединений и вызвать databaseClient() для создания Sql2o
         */
        var configuration = new DatasourceConfiguration();
        var datasource = configuration.connectionPool(url, username, password);
        var sql2o = configuration.databaseClient(datasource);

        /**
         * Клиент БД настроили. Теперь можно на основе него создать репозитории
         */
        sql2oVacancyRepository = new Sql2oVacancyRepository(sql2o);
        sql2oFileRepository = new Sql2oFileRepository(sql2o);

        /**
         * Создаем файл на который будут ссылаться вакансии.
         * Нужно сохранить хотя бы один файл, т.к. Vacancy от него зависит
         */
        file = new File("test", "test");
        sql2oFileRepository.save(file);
    }

    /**
     * После окончания всех тестов файл нужно удалить
     */
    @AfterAll
    public static void deleteFile() {
        sql2oFileRepository.deleteById(file.getId());
    }

    /**
     * Вакансии мы удаляем после каждого теста, для изолированности тестирования.
     * Изменения внесенные одним тестом не должны быть видны в другом.
     * Согласно принципу FIRST, буква I в нем означала Isolation, т.е. изолированность.
     * Метод clearVacancies(), как раз помогает нам ее достичь.
     */
    @AfterEach
    public void clearVacancies() {
        var vacancies = sql2oVacancyRepository.findAll();
        for (var vacancy : vacancies) {
            sql2oVacancyRepository.deleteById(vacancy.getId());
        }
    }

    /**
     * usingRecursiveComparison() указывает Junit, что нужно пройтись во всем свойствам объекта и сравнить их.
     * Если не вызвать этот метод, то Junit сравнит оба объекта через equals(), но нам это не подходит,
     * потому что нам equals() сравнивает только id в классе Vacancy.
     *
     * Еще один момент это вот эта строка: var creationDate = now().truncatedTo(ChronoUnit.MINUTES);
     * Дело в том, что БД H2 и JVM с разной точностью хранят дату и время.
     * Разница небольшая - несколько миллисекунд. Из-за этого тесты могут валиться иногда.
     * Чтобы этого не происходило, мы урезаем время до минут.
     */
    @Test
    public void whenSaveThenGetSame() {
        var creationDate = now().truncatedTo(ChronoUnit.MINUTES);
        var vacancy = sql2oVacancyRepository.save(new Vacancy(0, "title", "description", creationDate, true, 1, file.getId()));
        var savedVacancy = sql2oVacancyRepository.findById(vacancy.getId()).get();
        assertThat(savedVacancy).usingRecursiveComparison().isEqualTo(vacancy);
    }

    @Test
    public void whenSaveSeveralThenGetAll() {
        var creationDate = now().truncatedTo(ChronoUnit.MINUTES);
        var vacancy1 = sql2oVacancyRepository.save(new Vacancy(0, "title1", "description1", creationDate, true, 1, file.getId()));
        var vacancy2 = sql2oVacancyRepository.save(new Vacancy(0, "title2", "description2", creationDate, false, 1, file.getId()));
        var vacancy3 = sql2oVacancyRepository.save(new Vacancy(0, "title3", "description3", creationDate, true, 1, file.getId()));
        var result = sql2oVacancyRepository.findAll();
        assertThat(result).isEqualTo(List.of(vacancy1, vacancy2, vacancy3));
    }

    @Test
    public void whenDontSaveThenNothingFound() {
        assertThat(sql2oVacancyRepository.findAll()).isEqualTo(emptyList());
        assertThat(sql2oVacancyRepository.findById(0)).isEqualTo(empty());
    }

    @Test
    public void whenDeleteThenGetEmptyOptional() {
        var creationDate = now().truncatedTo(ChronoUnit.MINUTES);
        var vacancy = sql2oVacancyRepository.save(new Vacancy(0, "title", "description", creationDate, true, 1, file.getId()));
        var isDeleted = sql2oVacancyRepository.deleteById(vacancy.getId());
        var savedVacancy = sql2oVacancyRepository.findById(vacancy.getId());
        assertThat(isDeleted).isTrue();
        assertThat(savedVacancy).isEqualTo(empty());
    }

    @Test
    public void whenDeleteByInvalidIdThenGetFalse() {
        assertThat(sql2oVacancyRepository.deleteById(0)).isFalse();
    }

    @Test
    public void whenUpdateThenGetUpdated() {
        var creationDate = now().truncatedTo(ChronoUnit.MINUTES);
        var vacancy = sql2oVacancyRepository.save(new Vacancy(0, "title", "description", creationDate, true, 1, file.getId()));
        var updatedVacancy = new Vacancy(
                vacancy.getId(), "new title", "new description", creationDate.plusDays(1),
                !vacancy.getVisible(), 1, file.getId()
        );
        var isUpdated = sql2oVacancyRepository.update(updatedVacancy);
        var savedVacancy = sql2oVacancyRepository.findById(updatedVacancy.getId()).get();
        assertThat(isUpdated).isTrue();
        assertThat(savedVacancy).usingRecursiveComparison().isEqualTo(updatedVacancy);
    }

    @Test
    public void whenUpdateUnExistingVacancyThenGetFalse() {
        var creationDate = now().truncatedTo(ChronoUnit.MINUTES);
        var vacancy = new Vacancy(0, "title", "description", creationDate, true, 1, file.getId());
        var isUpdated = sql2oVacancyRepository.update(vacancy);
        assertThat(isUpdated).isFalse();
    }
}
