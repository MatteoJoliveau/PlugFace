package com.matteojoliveau.plugface.security;

import com.matteojoliveau.plugface.PluginClassLoader;
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