import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class MainTest {
    @ParameterizedTest
    @CsvSource(value = {
            "aaaaa, true",
            "aaaab, true",
            "aabbb, true",
            "aabcc, true",
            "aabaa, true",
            "abcba, true",
            "ccbba, false"
    })
    void isNiceNick(String text, boolean expected) {
        Assertions.assertEquals(Main.isNiceNick(text), expected);
    }
}