package ru.job4j.dreamjob.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Vasiliy Novopashin
 * @version 1.0
 * {@code @date} 05.03.2023
 */

@RestController
public class IndexController {
    @GetMapping("/index")
    public String getIndex() {
        return "Hello World!";
    }
}
