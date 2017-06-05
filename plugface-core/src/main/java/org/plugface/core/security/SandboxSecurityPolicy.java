package org.plugface.core.security;

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

import org.plugface.core.PluginClassLoader;

import java.security.*;

/**
 * Special set of {@link Policy} that wraps plugins in a sandbox, preventing
 * malicious code from causing damages.
 */
public class SandboxSecurityPolicy extends Policy {

    @Override
    public PermissionCollection getPermissions(ProtectionDomain domain) {
        if (isPlugin(domain)) {
            return pluginPermissions();
        } else {
            return applicationPermissions();
        }
    }

    /**
     * Checks wheter the loaded class is a plugin or not, by inspecting the {@link ClassLoader} of the
     * {@link ProtectionDomain}
     *
     * @param domain the {@link ProtectionDomain} to verify
     * @return true if the {@link ClassLoader} is of type {@link PluginClassLoader}, false if it's not
     */
    private boolean isPlugin(ProtectionDomain domain) {
        return (domain.getClassLoader() instanceof PluginClassLoader);
    }

    /**
     * Return an empty set of {@link Permissions} for plugins
     *
     * @return a {@link PermissionCollection} with empty {@link Permissions}
     */
    private PermissionCollection pluginPermissions() {
        Permissions permissions = new Permissions(); // No permissions
        return permissions;
    }

    /**
     * Return a set of {@link Permissions} for the main application, with all the permissions
     * granted.
     *
     * @return a {@link PermissionCollection} with an {@link AllPermission} permission.
     */
    private PermissionCollection applicationPermissions() {
        Permissions permissions = new Permissions();
        permissions.add(new AllPermission());
        return permissions;
    }
}
