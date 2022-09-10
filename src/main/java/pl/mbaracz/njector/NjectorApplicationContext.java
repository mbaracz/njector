package pl.mbaracz.njector;

import org.reflections.Reflections;
import org.reflections.scanners.Scanners;
import pl.mbaracz.njector.annotation.Bean;
import pl.mbaracz.njector.annotation.Component;
import pl.mbaracz.njector.annotation.Configuration;
import pl.mbaracz.njector.injector.ConstructorInjector;
import pl.mbaracz.njector.injector.FieldInjector;
import pl.mbaracz.njector.injector.SetterInjector;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class NjectorApplicationContext {

    protected final Map<Key, Object> singletons = new ConcurrentHashMap<>();
    protected final Map<Class<?>, Class<?>> diMap = new ConcurrentHashMap<>();

    private final ConstructorInjector constructorInjector;
    private final FieldInjector fieldInjector;
    private final SetterInjector setterInjector;

    public NjectorApplicationContext() {
        this.constructorInjector = new ConstructorInjector(this);
        this.fieldInjector = new FieldInjector(this);
        this.setterInjector = new SetterInjector(this);
    }

    protected void initializeApplicationContext(Class<?> mainClass) throws InstantiationException, IllegalAccessException, InvocationTargetException {
        Reflections reflections = new Reflections(mainClass.getPackage().getName(), Scanners.TypesAnnotated);
        Set<Class<?>> constructorClasses = new HashSet<>();
        Set<Class<?>> componentClasses = reflections.getTypesAnnotatedWith(Component.class);
        Set<Class<?>> configurationClasses = reflections.getTypesAnnotatedWith(Configuration.class);
        componentClasses.addAll(configurationClasses);

        for (Class<?> type : componentClasses) {
            if (type.getInterfaces().length == 0) {
                diMap.put(type, type);
            } else {
                for (Class<?> iface : type.getInterfaces()) {
                    diMap.put(type, iface);
                }
            }
            if (NjectUtil.hasNoArgsConstructor(type)) {
                singletons.put(Key.of(type, NjectUtil.getQualifier(type)), type.newInstance());
            } else {
                constructorClasses.add(type);
            }
        }

        for (Class<?> type : constructorClasses) {
            singletons.put(Key.of(type, NjectUtil.getQualifier(type)), constructorInjector.newInstance(type));
        }
        for (Class<?> type : configurationClasses) {
            for (Method method : NjectUtil.getAllMethods(type, Bean.class)) {
                String qualifier = method.getAnnotation(Bean.class).value();
                qualifier = qualifier.isEmpty() ? null : qualifier;
                diMap.put(method.getReturnType(), method.getReturnType());
                singletons.put(Key.of(method.getReturnType(), qualifier), NjectUtil.invokeMethod(this, method, getBean(type)));
            }
        }
        for (Map.Entry<Key, Object> entry : singletons.entrySet()) {
            fieldInjector.inject(entry.getKey().getType(), entry.getValue());
            setterInjector.inject(entry.getKey().getType(), entry.getValue());
        }
    }

    @SuppressWarnings("unchecked")
    public <T> T getBean(Class<T> type) {
        try {
            return (T) NjectUtil.getBeanInstance(this, type, null, null);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return null;
    }
}
