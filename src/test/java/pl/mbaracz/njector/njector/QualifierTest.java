package pl.mbaracz.njector.njector;

import org.junit.jupiter.api.Test;
import pl.mbaracz.njector.Njector;
import pl.mbaracz.njector.NjectorApplicationContext;
import pl.mbaracz.njector.annotation.Component;
import pl.mbaracz.njector.annotation.Qualifier;

import java.lang.reflect.InvocationTargetException;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class QualifierTest {

    public interface SomeTrait {
        String saySomething();
    }

    @Component
    @Qualifier("helloComponent")
    public static class HelloComponent implements SomeTrait {
        @Override
        public String saySomething() {
            return "hello";
        }
    }

    @Component
    @Qualifier("some crazy name")
    public static class WelcomeComponent implements SomeTrait {
        @Override
        public String saySomething() {
            return "welcome";
        }
    }

    @Component
    public static class Printer {
        private final SomeTrait hello;
        private final SomeTrait welcome;

        public Printer(@Qualifier("helloComponent") SomeTrait hello, @Qualifier("some crazy name") SomeTrait welcome) {
            this.hello = hello;
            this.welcome = welcome;
        }
    }

    @Test
    public void shouldCorrectInject() throws InvocationTargetException, InstantiationException, IllegalAccessException {
        NjectorApplicationContext context = Njector.createApplicationContext(getClass());
        Printer printer = context.getBean(Printer.class);
        assertEquals("hello", printer.hello.saySomething());
        assertEquals("welcome", printer.welcome.saySomething());
    }
}
