package org.plugface.core.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation marking a plugin class that needs to be injected during loading.
 *
 * Missing required plugins will cause an InstantiationException from the PluginManager,
 * optional will not and will have value <code>null</code>
 *
 * @author Matteo Joliveau
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.PARAMETER)
public @interface Require {
    boolean optional() default false;
}
