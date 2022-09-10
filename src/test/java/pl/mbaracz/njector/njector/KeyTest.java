package pl.mbaracz.njector.njector;

import org.junit.jupiter.api.Test;
import pl.mbaracz.njector.Key;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class KeyTest {

    @Test
    public void testKeysAreEqual() {
        Class<?> type = getClass();
        Map<Key, String> map = new HashMap<>();
        map.put(Key.of(type), "test");
        assertTrue(map.containsKey(Key.of(type)));
        assertEquals(Key.of(type), Key.of(type));
    }
}
