package ru.naumen.collection.task2;

import java.util.*;

/**
 * Дано:
 * <pre>
 * public class User {
 *     private String username;
 *     private String email;
 *     private byte[] passwordHash;
 *     …
 * }
 * </pre>
 * Нужно реализовать метод
 * <pre>
 * public static List<User> findDuplicates(Collection<User> collA, Collection<User> collB);
 * </pre>
 * <p>который возвращает дубликаты пользователей, которые есть в обеих коллекциях.</p>
 * <p>Одинаковыми считаем пользователей, у которых совпадают все 3 поля: username,
 * email, passwordHash. Дубликаты внутри коллекций collA, collB можно не учитывать.</p>
 * <p>Метод должен быть оптимален по производительности.</p>
 * <p>Пользоваться можно только стандартными классами Java SE.
 * Коллекции collA, collB изменять запрещено.</p>
 *
 * См. {@link User}
 *
 * @author vpyzhyanov
 * @since 19.10.2023
 */
public class Task2
{

    /**
     * Возвращает дубликаты пользователей, которые есть в обеих коллекциях
     * <p>
     * Причина выбора коллекции HashSet:
     * HashSet обеспечивает среднее время доступа O(1) для операций
     * добавления и проверки наличия элемента (в случае правильно
     * написанных {@link User#equals(Object)} и {@link User#hashCode()}), что делает его идеальным
     * выбором для хранения пользователей при поиске дубликатов.
     * Такой выбор коллекций обеспечивает оптимальную производительность и удобство
     * при реализации метода поиска дубликатов.
     * @return список дубликатов
     */
    public static List<User> findDuplicates(Collection<User> collA, Collection<User> collB) {
        if (collA.size() <= collB.size()) {
            return findCommonUsers(collB, new HashSet<>(collA));
        } else {
            return findCommonUsers(collA, new HashSet<>(collB));
        }

    }
    /**
     * Находит общих пользователей между коллекцией и множеством
     * <p>
     * Временная сложность: O(n), где n - размер коллекции usersToCheck,
     * так как для каждого пользователя выполняется операция проверки
     * наличия в множестве, которая в среднем выполняется за O(1).
     * </p>
     * Причина выбора ArrayList для хранения результата:
     * ArrayList обеспечивает эффективное добавление элементов
     * в конец списка с временем O(1), что делает
     * его подходящим выбором для хранения найденных общих пользователей.
     * <p>
     * Пространственная сложность: O(1), если не учитывать входные данные,
     * так как дополнительная память используется только для хранения
     * результата.
     * </p>
     * @return список общих пользователей
     */
    private static List<User> findCommonUsers (Collection<User> usersToCheck, HashSet<User> userSet) {
        List<User> result = new ArrayList<>();

        for (User user : usersToCheck) {
            if (userSet.contains(user)) {
                result.add(user);
            }
        }

        return result;
    }
}