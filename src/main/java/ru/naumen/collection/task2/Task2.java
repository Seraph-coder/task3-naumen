package ru.naumen.collection.task2;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
     *     Принцип работы:
     *     <ul>
     *         <li>Вычисляется размер обеих коллекций и их отношение</li>
     *         <li>Если отношение размеров больше 2, то меньшая коллекция
     *         преобразуется в HashSet, и по нему фильтруется большая коллекция</li>
     *         <li>Если отношение размеров меньше или равно 2, то первая
     *         коллекция преобразуется в HashSet, и по нему фильтруется вторая коллекция</li>
     *     </ul>
     *     Такой подход позволяет оптимизировать работу метода при значительной разнице в размерах
     *     коллекций, сохраняя при этом хорошую производительность при близких размерах.
     *     <br>
     *     Временная сложность в худшем случае: O(n + m), где n и m - размеры коллекций.
     *     Пространственная сложность: O(min(n, m)) для хранения меньшей коллекции в HashSet.
     *     <br>
     *     Таким образом, метод эффективно находит дубликаты пользователей в обеих коллекциях
     *     с оптимальной производительностью.
     *     </p>
     *     <P>
     *         Причина выбора коллекции HashSet:
     *         HashSet обеспечивает среднее время доступа O(1) для операций
     *         добавления и проверки наличия элемента (в случае правильно
     *         написанных {@link User#equals(Object)} и {@link User#hashCode()}), что делает его идеальным
     *         выбором для хранения пользователей при поиске дубликатов.
     *         Такой выбор коллекций обеспечивает оптимальную производительность и удобство
     *         при реализации метода поиска дубликатов.
     *     </P>
     *     Примечание: Метод {@link #findCommonUsers} используется для фильтрации одной коллекции
     *     на основе наличия элементов в множестве, что обеспечивает эффективный поиск общих
     *     пользователей.
     * @param collA коллекция A
     * @param collB коллекция B
     * @return список дубликатов
     */
    public static List<User> findDuplicates(Collection<User> collA, Collection<User> collB) {
        final double SIGNIFICANT_RATIO = 2.0;

        double sizeA = collA.size();
        double sizeB = collB.size();

        if (sizeA == 0 || sizeB == 0) {
            return List.of();
        }

        double ratio = Math.max(sizeA, sizeB) / Math.min(sizeA, sizeB);

        if (ratio > SIGNIFICANT_RATIO) {
            Collection<User> smallerCol = sizeA > sizeB ? collB : collA;
            Collection<User> largerCol = sizeA > sizeB ? collA : collB;

            Set<User> smallerSet = new HashSet<>(smallerCol);

            return findCommonUsers(largerCol, smallerSet);
        } else {

            Set<User> setA = new HashSet<>(collA);

            return findCommonUsers(collB, setA);
        }
    }
    /**
     * Находит общих пользователей между коллекцией и множеством
     * <br>
     * <p>Принцип работы:
     * Берём коллекцию пользователей для проверки и фильтруем её,
     * оставляя только тех пользователей, которые содержатся в множестве.
     * Использование множества (Set) позволяет эффективно проверять наличие
     * пользователя благодаря быстрому доступу по хешу.</p>
     * <p>
     * Временная сложность: O(n), где n - размер коллекции usersToCheck,
     * так как для каждого пользователя выполняется операция проверки
     * наличия в множестве, которая в среднем выполняется за O(1).
     * </p>
     * <p>
     * Пространственная сложность: O(1), если не учитывать входные данные,
     * так как дополнительная память используется только для хранения
     * результата.
     * </p>
     * Таким образом, метод эффективно находит общих пользователей
     * между коллекцией и множеством с оптимальной производительностью.
     *
     * @param usersToCheck коллекция пользователей для проверки
     * @param userSet множество пользователей для сравнения
     * @return список общих пользователей
     */
    private static List<User> findCommonUsers (Collection<User> usersToCheck, Set<User> userSet) {
        return  usersToCheck.stream()
                .filter(userSet::contains)
                .toList();
    }
}