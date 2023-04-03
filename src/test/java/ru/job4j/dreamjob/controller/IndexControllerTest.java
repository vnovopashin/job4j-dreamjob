package ru.job4j.dreamjob.controller;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

/**
 * Класс тестирует методы контроллера IndexController
 *
 * @author Vasiliy Novopashin
 * @version 1.0
 * {@code @date} 03.04.2023
 */
class IndexControllerTest {

    @Test
    public void whenRequestIndexThenGetPageIndex() {
        assertThat(new IndexController().getIndex()).isEqualTo("index");
    }
}
