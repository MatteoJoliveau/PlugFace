package org.plugface.impl;

/*-
 * #%L
 * plugface-core
 * %%
 * Copyright (C) 2017 Matteo Joliveau
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

import org.plugface.PluginConfiguration;
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
