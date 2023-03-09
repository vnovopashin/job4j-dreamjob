package ru.job4j.dreamjob.repository;

import net.jcip.annotations.ThreadSafe;
import org.springframework.stereotype.Repository;
import org.sql2o.Sql2o;
import ru.job4j.dreamjob.model.City;

import java.util.Collection;

/**
 * Класс реализует интерфейс CityRepository
 *
 * @author Vasiliy Novopashin
 * @version 1.0
 * {@code @date} 09.03.2023
 */

@ThreadSafe
@Repository
public class Sql2oCityRepository implements CityRepository {
    private final Sql2o sql2o;

    public Sql2oCityRepository(Sql2o sql2o) {
        this.sql2o = sql2o;
    }

    @Override
    public Collection<City> findAll() {
        try (var connection = sql2o.open()) {
            var query = connection.createQuery("SELECT * FROM cities");
            return query.executeAndFetch(City.class);
        }
    }
}
