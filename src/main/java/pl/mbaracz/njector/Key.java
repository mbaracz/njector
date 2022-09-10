package pl.mbaracz.njector;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Objects;

@Getter
@RequiredArgsConstructor
public class Key {

    private final Class<?> type;

    private final String qualifier;

    public static Key of(Class<?> type) {
        return new Key(type, null);
    }

    public static Key of(Class<?> type, String qualifier) {
        return new Key(type, qualifier);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, qualifier);
    }

    @Override
    public boolean equals(Object object) {
        if (object == this) {
            return true;
        }
        if (object instanceof Key) {
            Key key = (Key) object;
            return Objects.equals(key.type, this.type) && Objects.equals(key.qualifier, this.qualifier);
        }
        return false;
    }
}
