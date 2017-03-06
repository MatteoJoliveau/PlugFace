package com.matteojoliveau.plugface.impl;

import com.matteojoliveau.plugface.PluginConfiguration;
import org.junit.Assert;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class DefaultPluginConfigurationTest {

    @Test
    public void updateConfiguration() throws Exception {
        PluginConfiguration config = new DefaultPluginConfiguration();
        Map<String, Object> params = new HashMap<>();
        params.put("ParamOne", 1);
        params.put("ParamTwo", 2);
        params.put("ParamThree", 3);
        params.put("ParamFour", 4);

        config.updateConfiguration(params);

        Assert.assertEquals(params, config);
    }

    @Test
    public void setParams() throws Exception {
        PluginConfiguration config = new DefaultPluginConfiguration();

        config.setParameter("ParamOne", "One");

        Assert.assertEquals("One", config.getParameter("ParamOne"));
    }
}