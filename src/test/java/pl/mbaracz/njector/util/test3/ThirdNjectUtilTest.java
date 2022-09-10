package pl.mbaracz.njector.util.test3;

import org.junit.jupiter.api.Test;
import pl.mbaracz.njector.Njector;
import pl.mbaracz.njector.annotation.Component;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ThirdNjectUtilTest {

    @Component
    public static class SomeService {
        public SomeService(int unused) {}
    }

    @Test
    public void shouldThrowExceptionWhenNoImplementationFound() {
        Exception exception = assertThrows(RuntimeException.class, () -> Njector.createApplicationContext(getClass()));
        assertTrue(exception.getMessage().contains("No implementation found"));
    }
}
