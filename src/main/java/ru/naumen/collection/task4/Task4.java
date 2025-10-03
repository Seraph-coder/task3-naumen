package ru.naumen.collection.task4;

import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

/**
 * Реализовать класс управления расчётами {@link ConcurrentCalculationManager}
 * <p>Произвольные потоки добавляют задачи на вычисление, в основном потоке выводится
 * результат всех вычислений.<br>
 * Ожидание выполнения задачи не должно приводить к ошибке или получению null,
 * т.е. основной поток должен остановиться и продолжить работу только тогда, когда расчёт будет завершён.</p>
 * Требования:
 * <ul>
 *   <li>Задачу нужно решить только правильным выбором коллекции</li>
 *   <li>Использовать стандартные средства для работы с многопоточностью нельзя!
 *       (synchronised, ReentrantLock )</li>
 *   <li>Выполнение функций должно быть строго упорядочено. Система должна уметь выполнять
 *       несколько функций одновременно, но результат нужно выдавать строго в том порядке,
 *       в котором функции были переданы.</li>
 *   <li>Общее время работы программы не должно превышать время на наиболее долгую задачу
 *       (3 секунды + погрешность)</li>
 * </ul>
 * <b>Ожидаемый вывод программы должен быть в следующем порядке:</b>
 * <pre>
 *     Результат выполнения 1 : Задача №1
 *     Результат выполнения 2 : Задача №2
 *     Результат выполнения 3 : Задача №3
 * </pre>
 *
 * @author Пыжьянов Вячеслав
 * @since 04.07.2024
 */
public class Task4 {

    public static void main(String[] args) throws InterruptedException {
        ConcurrentCalculationManager<String> calculationManager = new ConcurrentCalculationManager<>();
        ExecutorService executorService = Executors.newFixedThreadPool(3);
        Instant startTimer = Instant.now();

        // Задачи добавляются в отдельных потоках
        executeTaskInSeparateThread(executorService, calculationManager, () -> {
            try {
                Thread.sleep(3000L);
            }
            catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            System.out.println("Завершена задача №1");
            return "Задача №1";
        });
        Thread.sleep(100L);
        executeTaskInSeparateThread(executorService, calculationManager, () -> {
            try {
                Thread.sleep(2000L);
            }
            catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            System.out.println("Завершена задача №2");
            return "Задача №2";
        });
        Thread.sleep(100L);
        executeTaskInSeparateThread(executorService, calculationManager, () -> {
            try {
                Thread.sleep(1000L);
            }
            catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            System.out.println("Завершена задача №3");
            return "Задача №3";
        });

        // Получение результата происходит в текущем потоке
        // Внимание! В этот момент результата ещё нет, поэтому нужно подождать.
        System.out.println("Результат выполнения 1: " + calculationManager.getResult());
        System.out.println("Результат выполнения 2: " + calculationManager.getResult());
        System.out.println("Результат выполнения 3: " + calculationManager.getResult());

        executorService.shutdown();
        executorService.awaitTermination(7, TimeUnit.SECONDS);

        Duration duration = Duration.between(startTimer, Instant.now());
        System.err.println("Total time = " +
                duration.getSeconds() + " seconds (" + duration.toMillis() + " millis)");
        // Ожидаемый вывод должен быть в порядке:
        //   Результат выполнения 1 : Задача №1
        //   Результат выполнения 2 : Задача №2
        //   Результат выполнения 3 : Задача №3
        // При этом время выполнения должно быть 3 секунды!
        if (duration.getSeconds() > 3) {
            throw new AssertionError("Время выполнения не должно превышать 3 секунды");
        }
    }

    /**
     * Выполнить задачу в отдельном потоке
     */
    private static <T> void executeTaskInSeparateThread(
            ExecutorService executorService,
            ConcurrentCalculationManager<T> calculationManager,
            Supplier<T> task) {
        executorService.submit(() -> {
            calculationManager.addTask(task);
        });
    }
}
