package org.plugface.security;

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

import org.plugface.PluginClassLoader;
import org.junit.Assert;
import org.junit.Test;

import java.net.URL;
import java.net.URLClassLoader;
import java.security.CodeSource;
import java.security.PermissionCollection;
import java.security.ProtectionDomain;

import static org.mockito.Mockito.mock;

public class SandboxSecurityPolicyTest {

    @Test
    public void getPluginPermissions() throws Exception {
        CodeSource cs = mock(CodeSource.class);
        ProtectionDomain domain = new ProtectionDomain(cs, null, new PluginClassLoader(new URL[0]), null);

        SandboxSecurityPolicy policy = new SandboxSecurityPolicy();

        PermissionCollection permissions = policy.getPermissions(domain);
        Assert.assertFalse(permissions.elements().hasMoreElements());

    }



    @Test
    public void getApplicationPermissions() throws Exception {
        CodeSource cs = mock(CodeSource.class);
        ProtectionDomain domain = new ProtectionDomain(cs, null, new URLClassLoader(new URL[0]), null);

        SandboxSecurityPolicy policy = new SandboxSecurityPolicy();

        PermissionCollection permissions = policy.getPermissions(domain);
        Assert.assertTrue(permissions.elements().hasMoreElements());
    }
}
