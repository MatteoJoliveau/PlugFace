package org.plugface.spring;

/*-
 * #%L
 * PlugFace :: Spring
 * %%
 * Copyright (C) 2017 - 2018 PlugFace
 * %%
 * Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in
all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
THE SOFTWARE.
 * #L%
 */

import org.plugface.core.PluginContext;
import org.plugface.core.impl.DefaultPluginManager;
import org.plugface.core.internal.AnnotationProcessor;
import org.plugface.core.internal.DependencyResolver;
import org.plugface.core.internal.di.MissingDependencyException;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import javax.inject.Inject;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class SpringPluginManager extends DefaultPluginManager implements ApplicationContextAware {
    private ApplicationContext applicationContext;

    public SpringPluginManager(PluginContext context, AnnotationProcessor annotationProcessor, DependencyResolver dependencyResolver) {
        super(context, annotationProcessor, dependencyResolver);
    }

    @Override
    protected Object create(Class<?> refClass) {
        final Constructor<?>[] constructors = refClass.getConstructors();
        for (Constructor<?> constructor : constructors) {

            try {
                final Class<?>[] parameterTypes = constructor.getParameterTypes();
                if (parameterTypes.length == 0) {
                    return refClass.newInstance();
                } else {
                    if (constructor.getAnnotation(Inject.class) != null) {
                        final Object[] deps = new Object[parameterTypes.length];
                        for (int i = 0; i < parameterTypes.length; i++) {
                            final Class<?> param = parameterTypes[i];
                            final Object dep = context.getPlugin(param);
                            if (dep != null) {
                                deps[i] = dep;
                            } else {
                                try {
                                    final Object bean = applicationContext.getBean(param);
                                    deps[i] = bean;
                                } catch (NoSuchBeanDefinitionException e) {
                                    throw new MissingDependencyException("No plugin found for type %s while it is required by %s", param.getName(), refClass.getName());
                                }
                            }
                        }
                        return constructor.newInstance(deps);
                    }
                }
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException | IllegalArgumentException ignored) {
            }
        }
        return null;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
