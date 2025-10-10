package ru.naumen.collection.task3;

import javax.swing.*;
import java.awt.*;
import java.nio.file.Path;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.PriorityQueue;

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
    private static final LinkedHashMap<String, Integer> wordCount = new LinkedHashMap<>();

    /**
     * Мини-куча для хранения 10 наименее используемых слов (по значению счетчика)
     * и Макси-куча для хранения 10 наиболее используемых слов (по значению счетчика)
     * <p>
     * В PriorityQueue первый элемент - наименьший (минимальный) по заданному компаратору.
     * Поэтому для low10 (наименее используемых) используется компаратор, который упорядочивает
     * элементы по убыванию (от большего к меньшему), чтобы на вершине очереди был элемент с наибольшим значением.
     * Для high10 (наиболее используемых) используется естественный порядок (по возрастанию),
     * чтобы на вершине очереди был элемент с наименьшим значением.
     * <p>
     * Таким образом, когда размер очереди превышает 10, удаляется элемент с наибольшим значением
     * для low10 и с наименьшим значением для high10, что позволяет эффективно
     * поддерживать только 10 наименее и наиболее используемых слов соответственно.
     */
    private static final PriorityQueue<Map.Entry<String, Integer>> low10 =
            new PriorityQueue<>(
                    (a, b) ->
                            b.getValue().compareTo(a.getValue())
            );

    private static final PriorityQueue<Map.Entry<String, Integer>> high10 =
            new PriorityQueue<>(
                    Map.Entry.comparingByValue()
            );

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
        new WordParser(WAR_AND_PEACE_FILE_PATH)
                .forEachWord(word -> wordCount.merge(word, 1, Integer::sum));

        counter();
        showResults(high10, low10);
    }

    /**
     * Заполняет две кучи (минимальную и максимальную) на основе данных из хеш-таблицы
     * <p>
     * Алгоритм:
     *     <ul>
     *         <li>Для каждого элемента в хеш-таблице добавить его в обе кучи</li>
     *         <li>Если размер кучи превышает 10, удалить корневой элемент
     *         (минимальный для high10 и максимальный для low10)</li>
     *     </ul>
     *     Таким образом, в каждой куче будут храниться только 10 наиболее и наименее используемых слов соответственно.
     * </p>
     * Сложность алгоритма: O(n log k), где n - количество уникальных слов, k - размер кучи (10 в данном случае).
     * Выбор алгоритма обоснован практической эффективностью:
     * использование куч позволяет эффективно поддерживать топ-k элементов
     * без необходимости сортировки всех элементов, что особенно важно при большом количестве уникальных слов.
     */
    private static void counter() {
        for (Map.Entry<String, Integer> entry : WarAndPeace.wordCount.entrySet()) {
            WarAndPeace.high10.offer(entry);
            WarAndPeace.low10.offer(entry);

            if (WarAndPeace.high10.size() > 10) WarAndPeace.high10.poll();
            if (WarAndPeace.low10.size() > 10) WarAndPeace.low10.poll();
        }
    }

    /**
     * Отображает результаты в графическом окне
     * <p>
     *     Алгоритм:
     *     <ul>
     *         <li>Создать окно с текстовой областью</li>
     *         <li>Сформировать строку с результатами из содержимого куч</li>
     *         <li>Отобразить окно с результатами</li>
     *     </ul>
     * </p>
     * @param high10 минимальная куча для хранения 10 наиболее используемых слов
     * @param low10 максимальная куча для хранения 10 наименее используемых слов
     */
    public static void showResults(PriorityQueue<Map.Entry<String, Integer>> high10,
                                   PriorityQueue<Map.Entry<String, Integer>> low10) {
        JFrame frame = new JFrame("Статистика слов");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setMinimumSize(new Dimension(400, 600));

        JTextArea textArea = new JTextArea(20, 35);
        textArea.setEditable(false);
        textArea.setFont(new Font("SansSerif", Font.PLAIN, 16));

        StringBuilder sb = new StringBuilder();
        sb.append("TOP 10 наиболее используемых слов:\n\n");
        high10.stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                .forEach(e -> sb.append(e
                        .getKey()).append(" - ").append(e.getValue()).append(" раз(а)\n"));

        sb.append("\n10 наименее используемых:\n\n");
        low10.stream()
                .sorted(Map.Entry.comparingByValue())
                .forEach(e -> sb.append(e
                        .getKey()).append(" - ").append(e.getValue()).append(" раз(а)\n"));

        textArea.setText(sb.toString());
        frame.add(new JScrollPane(textArea));
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
