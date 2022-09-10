package pl.mbaracz.njector.util.test4;

import org.junit.jupiter.api.Test;
import pl.mbaracz.njector.Njector;
import pl.mbaracz.njector.annotation.Component;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class FourthInjectUtilTest {

    public interface SomeInterface {}

    @Component
    public static class FirstComponent implements SomeInterface {}

    @Component
    public static class SecondComponent implements SomeInterface {}

    @Component
    public static class SomeService {
        public SomeService(SomeInterface unused) {}
    }

    @Test
    public void shouldThrowExceptionWhenNoQualifier() {
        Exception exception = assertThrows(RuntimeException.class, () -> Njector.createApplicationContext(getClass()));
        assertTrue(exception.getMessage().contains("Expected single implementation"));
    }
}
