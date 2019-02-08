package org.plugface.spring;

/*-
 * #%L
 * PlugFace :: Spring
 * %%
 * Copyright (C) 2017 - 2018 PlugFace
 * %%
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 * #L%
 */

import org.plugface.core.impl.DefaultPluginContext;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * A customized {@link DefaultPluginContext} to use in combination with
 * Spring Framework
 */
public class SpringPluginContext extends DefaultPluginContext implements ApplicationContextAware {

    private ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @Override
    public <T> T getPlugin(String pluginName) {
        final T plugin = super.getPlugin(pluginName);
        if (plugin == null) {
            try {
                return (T) applicationContext.getBean(pluginName);
            } catch (NoSuchBeanDefinitionException e) {
                return null;
            }
        }
        return plugin;
    }

    @Override
    public <T> T getPlugin(Class<T> pluginClass) {
        final T plugin = super.getPlugin(pluginClass);
        if (plugin == null) {
            try {
                return applicationContext.getBean(pluginClass);
            } catch (NoSuchBeanDefinitionException e) {
                return null;
            }
        }
        return plugin;
    }


    @Override
    public <T> boolean hasPlugin(Class<T> pluginClass) {
        final boolean has = super.hasPlugin(pluginClass);
        if (!has) {
            try {
                applicationContext.getBean(pluginClass);
                return true;
            } catch (NoSuchBeanDefinitionException e) {
                return false;
            }
        } else {
            return true;
        }
    }

    @Override
    public boolean hasPlugin(String pluginName) {
        return super.hasPlugin(pluginName) || applicationContext.containsBean(pluginName);
    }

}
