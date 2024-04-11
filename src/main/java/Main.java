import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Main {
    private static final int TEXTS_QUANTITY = 100_000;
    private static final int COUNTERS_QUANTITY = 3;
    private static final List<AtomicInteger> counters = new ArrayList<>(COUNTERS_QUANTITY);

    public static void main(String[] args) throws InterruptedException {
        IntStream.range(0, COUNTERS_QUANTITY).forEach(i -> counters.add(new AtomicInteger()));
        String[] texts = generateTexts();
        ExecutorService executorService = Executors.newFixedThreadPool(COUNTERS_QUANTITY);

        IntStream.range(0, COUNTERS_QUANTITY).forEach(i -> executorService.submit(() -> {
            System.out.println("Thread " + i + " started.");
            for (String text : texts) {
                if (text.length() == i + COUNTERS_QUANTITY) {
                    if (isNiceNick(text)) {
                        counters.get(i).incrementAndGet();
                    }
                }
            }
        }));

        executorService.awaitTermination(1, TimeUnit.SECONDS);
        executorService.shutdown();
        IntStream.range(0, COUNTERS_QUANTITY).forEach(i -> System.out.printf("%nКрасивых слов с длиной %s: %s шт", i + COUNTERS_QUANTITY, counters.get(i)));
    }

    // covered by @Test ( src -> test -> java -> MainTest.java )
    public static boolean isNiceNick(String text) {
        return
                new StringBuilder(text).reverse().toString().equals(text) // boolean isPolyandrous
                        || text.chars().allMatch(ch -> ch == text.charAt(0)) // boolean isSingleChar
                        || Stream.of(text.split("")).sorted().collect(Collectors.joining()).equals(text); // boolean isAscendingOrder
    }

    private static String[] generateTexts() {
        Random random = new Random();
        String[] texts = new String[TEXTS_QUANTITY];
        for (int i = 0; i < texts.length; i++) {
            texts[i] = generateText("abc", 3 + random.nextInt(3));
        }
        return texts;
    }

    private static String generateText(String letters, int length) {
        Random random = new Random();
        StringBuilder text = new StringBuilder();
        for (int i = 0; i < length; i++) {
            text.append(letters.charAt(random.nextInt(letters.length())));
        }
        return text.toString();
    }
}
