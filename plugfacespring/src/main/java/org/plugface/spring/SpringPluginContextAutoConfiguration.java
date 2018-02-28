package org.plugface.spring;

import org.plugface.core.old.PluginContext;
import org.plugface.core.old.PluginManager;
import org.plugface.core.old.impl.DefaultPluginManager;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
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
    @ConditionalOnBean(value = PluginContext.class)
    @ConditionalOnMissingBean
    public PluginManager springDefaultPluginManager(PluginContext ctx) {
        return new DefaultPluginManager("springDefaultPluginManager", ctx);
    }
}
