package pl.mbaracz.njector;

import pl.mbaracz.njector.annotation.Inject;
import pl.mbaracz.njector.annotation.Qualifier;

import java.lang.annotation.Annotation;
import java.lang.reflect.*;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class NjectUtil {

    private static final String MULTIPLE_IMPL_ERROR = "There are %d implementations of interface %s. Expected single implementation or make use of @Qualifier.";
    private static final String NO_IMPL_FOUND = "No implementation found for %s class.";

    /**
     * Check class has no-args constructor
     */
    public static boolean hasNoArgsConstructor(Class<?> type) {
        return Stream.of(type.getDeclaredConstructors()).anyMatch(constructor -> constructor.getParameterCount() == 0);
    }

    /**
     * Get all fields having inject annotation
     */
    public static Set<Field> getAllFields(Class<?> type) {
        Set<Field> fields = new HashSet<>();

        while (type != null) {
            for (Field field : type.getDeclaredFields()) {
                if (field.isAnnotationPresent(Inject.class)) {
                    fields.add(field);
                }
            }
            type = type.getSuperclass();
        }
        return fields;
    }

    /**
     * Get all methods with optional annotation
     */
    public static Set<Method> getAllMethods(Class<?> type, Class<? extends Annotation> annotation) {
        Set<Method> methods = new HashSet<>();

        while (type != null) {
            for (Method method : type.getDeclaredMethods()) {
                if (annotation == null || method.isAnnotationPresent(annotation)) {
                    methods.add(method);
                }
            }
            type = type.getSuperclass();
        }
        return methods;
    }

    /**
     * Overload getBeanInstance to handle qualifier and inject by type
     */
    public static Object getBeanInstance(NjectorApplicationContext context, Class<?> type, String fieldName, String qualifier) throws InstantiationException, IllegalAccessException {
        Class<?> implClass = getImplementationClass(context, type, fieldName, qualifier);

        Key key = Key.of(implClass, qualifier);
        if (context.singletons.containsKey(key)) {
            return context.singletons.get(key);
        }
        Object instance = implClass.newInstance();
        context.singletons.put(key, instance);
        return instance;
    }

    /**
     * Get implementation class for given class or interface
     */
    private static Class<?> getImplementationClass(NjectorApplicationContext context, Class<?> interfaceClass, String fieldName, String qualifier) {
        Set<Map.Entry<Class<?>, Class<?>>> implClasses = context.diMap.entrySet()
                .stream()
                .filter(entry -> entry.getValue() == interfaceClass)
                .collect(Collectors.toSet());

        if (implClasses.isEmpty()) {
            throw new RuntimeException(String.format(NO_IMPL_FOUND, interfaceClass.getName()));
        } else if (implClasses.size() == 1) {
            return implClasses.stream()
                    .findFirst()
                    .get()
                    .getKey();
        } else {
            return implClasses.stream()
                    .filter(entry -> checkQualifier(entry.getKey(), fieldName, qualifier))
                    .map(Map.Entry::getKey)
                    .findAny()
                    .orElseThrow(() -> new RuntimeException(String.format(MULTIPLE_IMPL_ERROR, implClasses.size(), interfaceClass.getName())));
        }
    }

    private static boolean checkQualifier(Class<?> key, String fieldName, String qualifier) {
        String classQualifier = getQualifier(key);
        classQualifier = classQualifier == null ? key.getSimpleName() : classQualifier;
        String findBy = (qualifier == null || qualifier.trim().length() == 0) ? fieldName : qualifier;
        return Objects.equals(classQualifier, findBy);
    }

    public static Object[] getParamInstances(NjectorApplicationContext context, Executable executable) throws InstantiationException, IllegalAccessException {
        Object[] paramInstances = new Object[executable.getParameterCount()];

        for (int i = 0; i < executable.getParameterCount(); i++) {
            Parameter param = executable.getParameters()[i];
            Class<?> paramClass = param.getType();
            String qualifier = param.isAnnotationPresent(Qualifier.class) ? param.getAnnotation(Qualifier.class).value() : null;
            paramInstances[i] = getBeanInstance(context, paramClass, param.getName(), qualifier);
        }
        return paramInstances;
    }

    public static String getQualifier(Class<?> type) {
        String qualifier = type.isAnnotationPresent(Qualifier.class) ? type.getAnnotation(Qualifier.class).value() : "";
        qualifier = qualifier.isEmpty() ? null : qualifier;
        return qualifier;
    }

    public static Object invokeMethod(NjectorApplicationContext context, Method method, Object classInstance) throws InstantiationException, IllegalAccessException, InvocationTargetException {
        Object[] paramInstances = getParamInstances(context, method);
        method.setAccessible(true);
        return method.invoke(classInstance, paramInstances);
    }
}
