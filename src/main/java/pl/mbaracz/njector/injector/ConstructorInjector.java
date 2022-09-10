package pl.mbaracz.njector.injector;

import lombok.RequiredArgsConstructor;
import pl.mbaracz.njector.NjectUtil;
import pl.mbaracz.njector.NjectorApplicationContext;
import pl.mbaracz.njector.annotation.Inject;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

@RequiredArgsConstructor
public class ConstructorInjector {

    private static final String NO_MATCHING_CONSTRUCTOR = "Class doesn't contain matching constructor for inject.";
    private static final String MULTIPLE_INJECT_ERROR = "Only one constructor can have @Inject annotation.";
    private final NjectorApplicationContext context;

    public Object newInstance(Class<?> type) throws InstantiationException, IllegalAccessException, InvocationTargetException {
        int injectAnnotated = 0;
        Object[] targetParamsInstances = new Object[]{};
        Constructor<?> targetConstructor = null;

        for (Constructor<?> constructor : type.getDeclaredConstructors()) {
            if (constructor.isAnnotationPresent(Inject.class)) {
                if (++injectAnnotated > 1) {
                    throw new RuntimeException(MULTIPLE_INJECT_ERROR);
                }
            } else if (type.getDeclaredConstructors().length > 1) {
                throw new RuntimeException(NO_MATCHING_CONSTRUCTOR);
            }
            targetConstructor = constructor;
            targetParamsInstances = NjectUtil.getParamInstances(context, constructor);
        }
        return targetConstructor.newInstance(targetParamsInstances);
    }
}
