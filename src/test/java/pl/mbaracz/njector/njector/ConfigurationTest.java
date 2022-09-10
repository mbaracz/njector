package pl.mbaracz.njector.njector;

import org.junit.jupiter.api.Test;
import pl.mbaracz.njector.Njector;
import pl.mbaracz.njector.NjectorApplicationContext;
import pl.mbaracz.njector.annotation.*;

import java.lang.reflect.InvocationTargetException;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ConfigurationTest {

    public interface Converter<I, O> {
        O convert(I i);
    }

    @Configuration
    public static class SimpleConfiguration {

        @Bean
        public Converter<String, Integer> createIntegerConverter() {
            return Integer::valueOf;
        }
    }

    @Component
    public static class SimpleConfigModule {

        @Inject
        private Converter<String, Integer> converter;

        public Integer test(String input) {
           return converter.convert(input);
       }
    }

    @Test
    public void checkConfigWorkProperly() throws InvocationTargetException, InstantiationException, IllegalAccessException {
        NjectorApplicationContext context = Njector.createApplicationContext(getClass());
        SimpleConfigModule module = context.getBean(SimpleConfigModule.class);
        assertEquals(1337, module.test("1337"));
    }
}
