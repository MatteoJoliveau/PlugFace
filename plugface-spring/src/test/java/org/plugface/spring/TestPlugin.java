package org.plugface.spring;

import org.plugface.core.annotations.Plugin;

import javax.inject.Inject;

@Plugin("test")
    public class TestPlugin {

        private final SpringPluginContextTest.TestBean bean;

        @Inject
        public TestPlugin(SpringPluginContextTest.TestBean bean) {
            this.bean = bean;
        }

        public String greet() {
            return "Plugface: " + bean.greet();
        }
    }