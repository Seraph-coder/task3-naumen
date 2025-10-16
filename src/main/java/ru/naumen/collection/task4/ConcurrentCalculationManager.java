package ru.naumen.collection.task4;

import java.util.concurrent.*;
import java.util.function.Supplier;

/**
 * Класс управления расчётами
 * {@link LinkedBlockingQueue} выбран за счет эффективной поддержки блокирующих операций
 * и минимизации блокировок между потоками. Ограничение размера очереди позволяет
 * контролировать количество одновременно выполняемых задач и предотвращает переполнение памяти.
 */
public class ConcurrentCalculationManager<T> {
    private final BlockingQueue<Future<T>> taskQueue = new LinkedBlockingQueue<>(3);
    /**
     * Добавить задачу на параллельное вычисление
     * @param task задача на вычисление
     */
    public void addTask(Supplier<T> task) {
        Future<T> future = CompletableFuture.supplyAsync(task);
        try {
            taskQueue.put(future);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Thread was interrupted", e);
        }
    }

    /**
     * Получить результат вычисления.
     * Возвращает результаты в том порядке, в котором добавлялись задачи.
     * BlockingQueue блокирует поток, если результат ещё не готов.
     * @return результат вычисления
     */
    public T getResult() {
        try {
            return taskQueue.take().get();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Thread was interrupted", e);
        } catch (ExecutionException e) {
            throw new RuntimeException("Task execution failed", e);
        }
    }
}