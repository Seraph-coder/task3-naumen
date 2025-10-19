package ru.naumen.collection.task3;


import java.nio.file.Path;
import java.util.*;

/**
 * <p>Написать консольное приложение, которое принимает на вход произвольный текстовый файл в формате txt.
 * Нужно собрать все встречающийся слова и посчитать для каждого из них количество раз, сколько слово встретилось.
 * Морфологию не учитываем.</p>
 * <p>Вывести на экран наиболее используемые (TOP) 10 слов и наименее используемые (LAST) 10 слов</p>
 * <p>Проверить работу на романе Льва Толстого “Война и мир”</p>
 *
 * @author vpyzhyanov
 * @since 19.10.2023
 */
public class WarAndPeace
{
    private static final Path WAR_AND_PEACE_FILE_PATH = Path.of("src/main/resources",
            "Лев_Толстой_Война_и_мир_Том_1,_2,_3,_4_(UTF-8).txt");

    /**
     * Хеш-таблица для хранения уникальных слов и их частоты встречаемости
     */
    private final Map<String, Integer> wordCount;

    /**
     * Мини-куча для хранения 10 наименее используемых слов (по значению счетчика)
     * и Макси-куча для хранения 10 наиболее используемых слов (по значению счетчика)
     */
    private final PriorityQueue<Map.Entry<String, Integer>> top10;

    private final PriorityQueue<Map.Entry<String, Integer>> last10;

    /**
     * Точка входа в программу
     * <br>
     * <p>Алгоритм:
     * <ul>
     *     <li>Считать файл построчно</li>
     *     <li>Разбить каждую строку на слова, отфильтровать слова короче 3 символов, привести к нижнему регистру</li>
     *     <li>Для каждого слова увеличить счетчик в хеш-таблице</li>
     *     <li>После подсчета всех слов пройти по хеш-таблице и заполнить две кучи (минимальную и максимальную)</li>
     *     <li>Вывести содержимое куч на экран</li>
     * </ul></p>
     * @param args аргументы командной строки (не используются)
     */
    public static void main(String[] args) {
        WarAndPeace app = new WarAndPeace();
        app.run();
    }

    /**
     * Конструктор класса WarAndPeace, который инициализирует необходимые
     * для решения задачи структуры данных
     * LinkedHashMap для хранения слов и их частоты встречаемости (реализация
     * выбрана из-за скорости итерации по записям)
     * PriorityQueue для хранения 10 наиболее и наименее используемых слов
     * (реализация выбрана из-за эффективного доступа к минимальному и
     * максимальному элементам)
     */
    public WarAndPeace() {
        this.wordCount = new LinkedHashMap<>();
        this.top10 = new PriorityQueue<>(11,
                (a, b) ->
                        b.getValue().compareTo(a.getValue())
        );
        this.last10 = new PriorityQueue<>(11,
                Map.Entry.comparingByValue()
        );
    }

    public void run() {
        new WordParser(WAR_AND_PEACE_FILE_PATH)
                .forEachWord(word -> wordCount.merge(word, 1, Integer::sum));

        populatePriorityQueues(wordCount, top10, last10);
        showResults(last10, top10);
    }

    /**
     * Заполняет две кучи (минимальную и максимальную) на основе данных из хеш-таблицы
     * Алгоритм:
     * <ul>
     *     <li>Для каждой записи в хеш-таблице проверить, можно ли добавить ее в кучу</li>
     *     <li>Если размер кучи меньше 10, просто добавить запись</li>
     *     <li>Если размер кучи равен 10, сравнить значение текущей записи с корневым элементом кучи
     *     (наибольшим для минимальной кучи и наименьшим для максимальной кучи)</li>
     *     <li>Если значение текущей записи больше корневого элемента минимальной кучи
     *     или меньше корневого элемента максимальной кучи, заменить корневой элемент на текущую запись</li>
     * </ul>
     */
    private static void populatePriorityQueues(Map<String, Integer> wordCount,
                                               PriorityQueue<Map.Entry<String, Integer>> top10,
                                               PriorityQueue<Map.Entry<String, Integer>> last10) {
        for (Map.Entry<String, Integer> entry : wordCount.entrySet()) {
            if (last10.size() < 10) {
                last10.offer(entry);
            } else if (entry.getValue() > last10.peek().getValue()) {
                last10.poll();
                last10.offer(entry);
            }

            if (top10.size() < 10) {
                top10.offer(entry);
            } else if (entry.getValue() < top10.peek().getValue()) {
                top10.poll();
                top10.offer(entry);
            }
        }
    }

    /**
     * Отображает результаты в консоли
     */
    public static void showResults(PriorityQueue<Map.Entry<String, Integer>> high10,
                                   PriorityQueue<Map.Entry<String, Integer>> low10) {
        List<Map.Entry<String, Integer>> topList = new ArrayList<>(high10);
        topList.sort((a, b) -> b.getValue().compareTo(a.getValue()));

        List<Map.Entry<String, Integer>> lowList = new ArrayList<>(low10);
        lowList.sort(Map.Entry.comparingByValue());

        StringBuilder sb = new StringBuilder();

        sb.append("TOP 10 наиболее используемых слов:\n\n");

        for (Map.Entry<String, Integer> entry : topList) {
            sb.append(entry.getKey()).append(" - ")
                    .append(entry.getValue()).append(" раз(а)\n");
        }

        sb.append("\n10 наименее используемых:\n\n");

        for (Map.Entry<String, Integer> entry : lowList) {
            sb.append(entry.getKey()).append(" - ")
                    .append(entry.getValue()).append(" раз(а)\n");
        }

        System.out.println(sb);
    }
}
