package pl.mbaracz.njector.njector;

import org.junit.jupiter.api.Test;
import pl.mbaracz.njector.Njector;

import java.lang.reflect.InvocationTargetException;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class NjectorTest {

    @Test
    public void shouldCorrectCreateApplicationContext() throws InvocationTargetException, InstantiationException, IllegalAccessException {
        assertNotNull(Njector.createApplicationContext(getClass()));
    }

    @Test
    public void shouldThrowExceptionWhenMainClassIsNull() {
        assertThrows(IllegalArgumentException.class, () -> Njector.createApplicationContext(null));
    }
}
