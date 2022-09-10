package pl.mbaracz.njector.util.test2;

import org.junit.jupiter.api.Test;
import pl.mbaracz.njector.Njector;
import pl.mbaracz.njector.annotation.Component;
import pl.mbaracz.njector.annotation.Inject;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class SecondNjectUtilTest {

    @Component
    public static class SomeComponent {}

    @Component
    public static class ExampleService {
        @Inject public ExampleService(SomeComponent component) {}
        @Inject public ExampleService(SomeComponent component1, SomeComponent component2) {}
    }

    @Test
    public void shouldThrowExceptionWhenMultipleConstructorsWithInjectAnnotation() {
        Exception exception = assertThrows(RuntimeException.class, () -> Njector.createApplicationContext(getClass()));
        assertEquals("Only one constructor can have @Inject annotation.", exception.getMessage());
    }
}
