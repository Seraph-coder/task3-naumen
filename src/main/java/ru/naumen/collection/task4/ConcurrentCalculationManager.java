package ru.naumen.collection.task4;

import java.util.function.Supplier;

/**
 * Класс управления расчётами
 */
public class ConcurrentCalculationManager<T> {

    /**
     * Добавить задачу на параллельное вычисление
     */
    public void addTask(Supplier<T> task) {
        // TODO реализовать
    }

    /**
     * Получить результат вычисления.
     * Возвращает результаты в том порядке, в котором добавлялись задачи.
     */
    public T getResult() {
        // TODO реализовать
        return null;
    }
}