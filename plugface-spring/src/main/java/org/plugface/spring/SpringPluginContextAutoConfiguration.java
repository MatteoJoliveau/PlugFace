package org.plugface.spring;

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