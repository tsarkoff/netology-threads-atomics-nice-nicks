import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Main {
    private static final String TEXT_LETTERS = "abc";   // letters are used in nicknames
    private static final int TEXTS_QUANTITY = 100_000;  // number of nicks are generated
    private static final int COUNTERS_QUANTITY = 3;     // number of threads / variants of nicknames lengths
    private static final List<AtomicInteger> counters = new ArrayList<>(COUNTERS_QUANTITY); // variable is to store nice nicknames number from Threads

    // MAIN INIT & THREADS
    public static void main(String[] args) throws InterruptedException {
        String[] texts = generateTexts();
        ExecutorService executorService = Executors.newFixedThreadPool(COUNTERS_QUANTITY);
        IntStream.range(0, COUNTERS_QUANTITY).forEach(i -> {
            counters.add(new AtomicInteger());
            executorService.submit(() -> {
                System.out.println("Thread " + i + " started.");
                for (String text : texts) {
                    if (text.length() == i + COUNTERS_QUANTITY && isNiceNick(text)) {
                        counters.get(i).incrementAndGet();
                    }
                }
            });
        });

        if (!executorService.awaitTermination(1, TimeUnit.SECONDS)) {
            System.out.println("executorService has not managed to await Termination by timeout ^ still wait for Threads finishing... ");
        }
        executorService.shutdown();
        IntStream.range(0, COUNTERS_QUANTITY).forEach(i -> System.out.printf("Красивых слов с длиной %s: %s шт%n", i + COUNTERS_QUANTITY, counters.get(i)));
    }

    // CHECK IF NICK IS NICE = covered by @Test ( src -> test -> java -> MainTest.java )
    public static boolean isNiceNick(String text) {
        return
                new StringBuilder(text).reverse().toString().equals(text)                                // boolean isPolyandrous
                || text.chars().allMatch(ch -> ch == text.charAt(0))                                     // boolean isSingleChar
                || Stream.of(text.split("")).sorted().collect(Collectors.joining()).equals(text); // boolean isAscendingOrder
    }

    // GENERATE 100_000 NICKNAMES
    private static String[] generateTexts() {
        Random random = new Random();
        String[] texts = new String[TEXTS_QUANTITY];
        for (int i = 0; i < texts.length; i++) {
            texts[i] = generateText(COUNTERS_QUANTITY + random.nextInt(COUNTERS_QUANTITY));
        }
        return texts;
    }

    // GENERATE 1 (one) NICKNAME
    private static String generateText(int length) {
        Random random = new Random();
        StringBuilder text = new StringBuilder();
        for (int i = 0; i < length; i++) {
            text.append(TEXT_LETTERS.charAt(random.nextInt(TEXT_LETTERS.length())));
        }
        return text.toString();
    }
}
