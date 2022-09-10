package pl.mbaracz.njector.injector;

import lombok.RequiredArgsConstructor;
import pl.mbaracz.njector.NjectUtil;
import pl.mbaracz.njector.NjectorApplicationContext;
import pl.mbaracz.njector.annotation.Inject;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

@RequiredArgsConstructor
public class SetterInjector {

    private final NjectorApplicationContext context;

    public void inject(Class<?> type, Object classInstance) throws InvocationTargetException, IllegalAccessException, InstantiationException {
        for (Method method : NjectUtil.getAllMethods(type, Inject.class)) {
            NjectUtil.invokeMethod(context, method, classInstance);
        }
    }
}
