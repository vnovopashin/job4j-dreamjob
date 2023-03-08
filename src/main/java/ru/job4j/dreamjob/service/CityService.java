package ru.job4j.dreamjob.service;


import ru.job4j.dreamjob.model.City;

import java.util.Collection;

/**
 * Интерфейс инкапсулирующий логику приложения
 *
 * @author Vasiliy Novopashin
 * @version 1.0
 * {@code @date} 08.03.2023
 */

public interface CityService {

    Collection<City> findAll();
}
