package pl.mbaracz.njector.njector;

import org.junit.jupiter.api.Test;
import pl.mbaracz.njector.Njector;
import pl.mbaracz.njector.NjectorApplicationContext;
import pl.mbaracz.njector.annotation.Component;
import pl.mbaracz.njector.annotation.Inject;

import java.lang.reflect.InvocationTargetException;

import static org.junit.jupiter.api.Assertions.*;

public class SetterInjectionTest {

    @Component
    public static class SomeManager {
        private MessageSender sender;

        @Inject
        public void setMessageSender(MessageSender sender) {
            this.sender = sender;
        }

        public String sendMessage(String message) {
            return sender.send(message);
        }
    }

    @Component
    public static class MessageSender {
        public String send(String text) {
            return text;
        }
    }

    @Test
    public void shouldCorrectInject() throws InvocationTargetException, InstantiationException, IllegalAccessException {
        NjectorApplicationContext context = Njector.createApplicationContext(getClass());
        SomeManager manager = context.getBean(SomeManager.class);
        String message = "Hello world!";
        assertEquals(message, manager.sendMessage(message));
    }
}
