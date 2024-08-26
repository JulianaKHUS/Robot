import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class Main {

    public static final Map<Integer, Integer> sizeToFreq = new HashMap<>();

    public static void main(String[] args) {
        int numThreads = 1000;

        Thread[] threads = new Thread[numThreads];
        for (int i = 0; i < numThreads; i++) {
            threads[i] = new Thread(new RouteGenerator());
            threads[i].start();
        }

        for (Thread thread : threads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        int maxFreq = 0;
        int maxFreqSize = 0;
        for (Map.Entry<Integer, Integer> entry : sizeToFreq.entrySet()) {
            int size = entry.getKey();
            int freq = entry.getValue();
            if (freq > maxFreq) {
                maxFreq = freq;
                maxFreqSize = size;
            }
        }

        System.out.println("Самое частое количество повторений " + maxFreqSize + " (встретилось " + maxFreq + " раз)");
        System.out.println("Другие размеры:");
        for (Map.Entry<Integer, Integer> entry : sizeToFreq.entrySet()) {
            if (entry.getKey() != maxFreqSize) {
                System.out.println("- " + entry.getKey() + " (" + entry.getValue() + " раз)");
            }
        }
    }

    public static String generateRoute(String letters, int length) {
        Random random = new Random();
        StringBuilder route = new StringBuilder();
        for (int i = 0; i < length; i++) {
            route.append(letters.charAt(random.nextInt(letters.length())));
        }
        return route.toString();
    }

    public static void updateFreq(int size) {
        synchronized (sizeToFreq) {
            sizeToFreq.put(size, sizeToFreq.getOrDefault(size, 0) + 1);
        }
    }

    static class RouteGenerator implements Runnable {
        @Override
        public void run() {
            String route = generateRoute("RLRFR", 100);
            int freq = 0;
            for (int i = 0; i < route.length(); i++) {
                if (route.charAt(i) == 'R') {
                    freq++;
                }
            }
            updateFreq(freq);
            System.out.println("Количество команд поворота направо: " + freq);
        }
    }
}
