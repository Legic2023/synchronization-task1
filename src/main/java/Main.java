import java.util.*;

public class Main {
    public static final Map<Integer, Integer> sizeToFreq = new HashMap<>();

    public static void main(String[] args) throws InterruptedException {
        int routesCount = 1000;
        int routeLength = 100;

        for (int i = 0; i < routesCount; i++) {
            Runnable runnable = () -> {
                String routeString = generateRoute("RLRFR", routeLength); // генерируем маршрут
                int countR = countCharOccurrences(routeString, 'R');// считаем количество символов 'R' в маршруте
                fillingMap(countR, sizeToFreq); // заполняем Map
                System.out.println(routeString + " -> " + countR);
            };
            Thread myThread = new Thread(runnable);
            myThread.start();
            myThread.join(); // ждем завершения потоков до вывода итогов
        }
        System.out.printf("Самое частое количество повторений: %s (встречается %s раз)%n",
                keyMapByMaxValue(sizeToFreq), sizeToFreq.get(keyMapByMaxValue(sizeToFreq)));
        System.out.println("Остальные значения:");
        System.out.println(sizeToFreq.size());
        sizeToFreq.forEach((key, value) -> System.out.printf(" - %s (встречается %s раз)%n", key, value));
    }

    // Функция генерирования маршрута
    public static String generateRoute(String letters, int length) {
        Random random = new Random();
        StringBuilder route = new StringBuilder();
        for (int i = 0; i < length; i++) {
            route.append(letters.charAt(random.nextInt(letters.length())));
        }
        return route.toString();
    }

    // Функция подсчета вхождений символа
    public static int countCharOccurrences(String string, char ch) {
        return (int) string.chars()
                .filter(c -> c == ch)
                .count();
    }

    // Функция поиска max значения в Map (отдает ключ)
    public static int keyMapByMaxValue(Map<Integer, Integer> map) {
        Optional<Map.Entry<Integer, Integer>> maxEntry = map.entrySet().stream()
                .max(Comparator.comparing(Map.Entry::getValue));

        return maxEntry.get().getKey();
    }

    // Функция заполнения Map ключами и значениями (критическая секция синхронизации)
    public static synchronized void fillingMap(int countR, Map<Integer, Integer> sizeToFreq) {
        if (sizeToFreq.containsKey(countR)) {
            int keyCount = sizeToFreq.get(countR) + 1; // ключ есть - увеличиваем значение на 1
            sizeToFreq.put(countR, keyCount);
        } else {
            sizeToFreq.put(countR, 1); // нет ключа - создаем со значением 1
        }
    }
}
