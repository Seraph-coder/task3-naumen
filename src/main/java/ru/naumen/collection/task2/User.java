package ru.naumen.collection.task2;

import java.util.Arrays;
import java.util.Objects;

/**
 * Пользователь
 * <br>
 * Класс с переопределёнными методами equals и hashCode для корректного сравнения объектов User.
 * Метод equals сравнивает все три поля: username, email и passwordHash
 * (используется Arrays.deepEquals для сравнения массивов).
 * Метод hashCode генерирует хеш-код на основе всех трёх полей, используя Arrays.hashCode
 * для массива passwordHash.
 * @author vpyzhyanov
 * @since 19.10.2023
 */
public class User {
    private String username;
    private String email;
    private byte[] passwordHash;

    /**
     * Сравнение объектов User
     * @param o   объект для сравнения
     * @return true, если объекты равны, иначе false
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(username, user.username) && Objects.equals(email, user.email) && Objects.deepEquals(passwordHash, user.passwordHash);
    }

    /**
     * Получение хеш-кода объекта User
     * @return
     */
    @Override
    public int hashCode() {
        return Objects.hash(username, email, Arrays.hashCode(passwordHash));
    }
}