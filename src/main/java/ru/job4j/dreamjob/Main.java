package ru.job4j.dreamjob;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author Vasiliy Novopashin
 * @version 1.0
 * {@code @date} 05.03.2023
 */

@SpringBootApplication
public class Main {
    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
        System.out.println("Go to http://localhost:8081/index");
    }
}
