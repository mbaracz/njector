package pl.mbaracz.njector;

import java.lang.reflect.InvocationTargetException;

public class Njector {

    public static NjectorApplicationContext createApplicationContext(Class<?> mainClass) throws InvocationTargetException, InstantiationException, IllegalAccessException {
        if (mainClass == null) {
            throw new IllegalArgumentException();
        }
        synchronized (Njector.class) {
            NjectorApplicationContext context = new NjectorApplicationContext();
            context.initializeApplicationContext(mainClass);
            return context;
        }
    }
}
