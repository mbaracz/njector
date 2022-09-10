package pl.mbaracz.njector.util.test1;

import org.junit.jupiter.api.Test;
import pl.mbaracz.njector.NjectUtil;
import pl.mbaracz.njector.Njector;
import pl.mbaracz.njector.annotation.Component;
import pl.mbaracz.njector.annotation.Inject;

import java.lang.reflect.Field;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

public class FirstNjectUtilTest {

    @Component
    public static class SomeComponent {}

    @Component
    public static class SomeService {
        public SomeService(SomeComponent component) {}
        public SomeService(SomeComponent component1, SomeComponent component2) {}
    }

    public static class A {
        @Inject public String a1 = "a1";
    }

    public static class B extends A {
        @Inject public String b1 = "b1";
    }

    public static class C extends B {
        @Inject public String c1 = "c1";
    }

    @Test
    public void checkAllFieldsFound() {
        Field[] fields = Stream.of(C.class.getDeclaredFields(), B.class.getDeclaredFields(), A.class.getDeclaredFields())
                .flatMap(Stream::of)
                .toArray(Field[]::new);

        assertArrayEquals(fields, NjectUtil.getAllFields(C.class).toArray());
    }

    @Test
    public void shouldThrowExceptionWhenMultipleConstructorsAvailable() {
        Exception exception = assertThrows(RuntimeException.class, () -> Njector.createApplicationContext(getClass()));
        assertEquals("Class doesn't contain matching constructor for inject.", exception.getMessage());
    }
}
