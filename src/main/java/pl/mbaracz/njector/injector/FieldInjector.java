package pl.mbaracz.njector.injector;

import lombok.RequiredArgsConstructor;
import pl.mbaracz.njector.NjectUtil;
import pl.mbaracz.njector.NjectorApplicationContext;
import pl.mbaracz.njector.annotation.Qualifier;

import java.lang.reflect.Field;

@RequiredArgsConstructor
public class FieldInjector {

    private final NjectorApplicationContext context;

    public void inject(Class<?> type, Object classInstance) throws InstantiationException, IllegalAccessException {
        for (Field field : NjectUtil.getAllFields(type)) {
            String qualifier = field.isAnnotationPresent(Qualifier.class) ? field.getAnnotation(Qualifier.class).value() : null;
            Object fieldInstance = NjectUtil.getBeanInstance(context, field.getType(), field.getName(), qualifier);
            field.setAccessible(true);
            field.set(classInstance, fieldInstance);
            inject(fieldInstance.getClass(), fieldInstance);
        }
    }
}
