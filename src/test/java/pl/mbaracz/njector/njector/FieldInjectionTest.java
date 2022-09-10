package pl.mbaracz.njector.njector;

import org.junit.jupiter.api.Test;
import pl.mbaracz.njector.Njector;
import pl.mbaracz.njector.NjectorApplicationContext;
import pl.mbaracz.njector.annotation.Component;
import pl.mbaracz.njector.annotation.Inject;

import java.lang.reflect.InvocationTargetException;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

public class FieldInjectionTest {

    @Component
    public static class ExampleModule {

    }

    @Component
    public static class MainModule {
        private ExampleModule notAnnotatedModule;

        @Inject
        private ExampleModule annotatedModule;
    }

    @Test
    public void shouldCorrectInject() throws InvocationTargetException, InstantiationException, IllegalAccessException {
        NjectorApplicationContext context = Njector.createApplicationContext(getClass());
        MainModule main = context.getBean(MainModule.class);
        assertNull(main.notAnnotatedModule);
        assertNotNull(main.annotatedModule);
    }
}
