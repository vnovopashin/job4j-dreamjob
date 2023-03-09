package ru.job4j.dreamjob.dto;

/**
 * Класс используется для передачи данных между слоями приложения,
 * в данном случае доменная модель File не соответствует тому,
 * что должна возвращать система, поэтому мы создаем дополнительную структуру,
 * чтобы вернуть веб-клиенту то, что нужно ему, а в БД хранить, то что нужно нам.
 *
 * @author Vasiliy Novopashin
 * @version 1.0
 * {@code @date} 08.03.2023
 */

public class FileDto {
    private String name;

    private byte[] content; /*Тут кроется различие. Доменная модель хранит путь, а не содержимое*/

    public FileDto(String name, byte[] content) {
        this.name = name;
        this.content = content;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public byte[] getContent() {
        return content;
    }

    public void setContent(byte[] content) {
        this.content = content;
    }
}
