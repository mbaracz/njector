package pl.mbaracz.njector.annotation;

import java.lang.annotation.*;

/**
 * Annotation used to differentiate beans when we have more than one bean of the same type
 */
@Inherited
@Target({ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER, ElementType.TYPE, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface Qualifier {

    String value() default "";
}