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

import org.plugface.core.PluginContext;
import org.plugface.core.PluginManager;
import org.plugface.core.internal.AnnotationProcessor;
import org.plugface.core.internal.DependencyResolver;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SpringPluginContextAutoConfiguration {
    @Bean
    @ConditionalOnMissingBean
    public PluginContext pluginContext(ApplicationContext context) {
        SpringPluginContext springPluginContext = new SpringPluginContext();
        springPluginContext.setApplicationContext(context);
        return springPluginContext;
    }

    @Bean
    @ConditionalOnMissingBean
    public AnnotationProcessor pluginAnnotationProcessor() {
        return new AnnotationProcessor();
    }

    @Bean
    @ConditionalOnMissingBean
    public DependencyResolver pluginResolver(AnnotationProcessor processor) {
        return new DependencyResolver(processor);
    }

    @Bean
    @ConditionalOnMissingBean
    public PluginManager springDefaultPluginManager(PluginContext ctx, AnnotationProcessor processor, DependencyResolver resolver) {
        return new SpringPluginManager(ctx, processor, resolver);
    }
}
