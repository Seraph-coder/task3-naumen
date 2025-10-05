package ru.naumen.collection.task1;

/**
 * Билет
 *
 * @author vpyzhyanov
 * @since 19.10.2023
 */
public class Ticket {
    private long id;
    private String client;

    /**
     * Сравнивает текущий билет с другим объектом на равенство.
     * Используется для корректной работы HashMap
     *
     * @param o   Объект, с которым выполняется сравнение
     * @return    {@code true}, если оба объекта представляют один и тот же билет (по {@code id});
     *            {@code  false} - в ином случае
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true; // Быстрая проверка для случая совпадения ссылок
        if (o == null || getClass() != o.getClass()) return false; // Проверка типа объекта
        Ticket ticket = (Ticket) o;
        return id == ticket.id; // Сравнение по id
    }

    /**
     * Возвращает хеш-код билета.
     * Хеш-код рассчитывается на основе {@code id}, для согласования с {@link #equals(Object)}.
     * @return хеш-код, вычисленный из id билета
     */
    @Override
    public int hashCode() {
        return Long.hashCode(id);
    }
}
