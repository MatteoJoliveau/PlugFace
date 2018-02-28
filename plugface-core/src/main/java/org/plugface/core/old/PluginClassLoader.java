package org.plugface.core.old;

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

import org.plugface.core.old.security.SandboxSecurityPolicy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.JarURLConnection;
import java.net.NetPermission;
import java.net.URL;
import java.net.URLClassLoader;
import java.security.*;
import java.util.Properties;

/**
 * Custom {@link URLClassLoader} that is used to load {@link Plugin} classes from Jar files.
 * <p>
 * It is used to give an empty set of permissions to plugins in {@link SandboxSecurityPolicy#isPlugin(ProtectionDomain)}
 * so that plugins run in a protected sandbox.
 * It also overrides the {@link URLClassLoader#getPermissions(CodeSource)} method so that
 * custom permissions specified in a <i>permissions.properties</i> file can be applied to specific plugins.
 * </p>
 */
public class PluginClassLoader extends URLClassLoader {
    private static final Logger LOGGER = LoggerFactory.getLogger(PluginClassLoader.class);

    /**
     * The default directory separator for Unix-like operating systems
     */
    private static final char UNIX_SEPARATOR = '/';

    /**
     * The default directory separator for Windows operating systems
     */
    private static final char WINDOWS_SEPARATOR = '\\';

    /**
     * The properties containing custom permissions for plugins
     */
    private Properties permissionProperties;

    /**
     * Construct a {@link PluginClassLoader} from an single {@link URL}
     *
     * @param jarFileUrl an Jar file {@link URL}
     */
    public PluginClassLoader(URL jarFileUrl) {
        super(new URL[]{jarFileUrl});
    }

    /**
     * Construct a {@link PluginClassLoader} from an array of {@link URL}
     *
     * @param jarFileUrls the array of Jar file {@link URL}
     */
    public PluginClassLoader(URL[] jarFileUrls) {
        super(jarFileUrls);
    }

    /**
     * Create a set of {@link Permission} for the specified {@link CodeSource}, given a
     * {@link PluginClassLoader#permissionProperties}.
     *
     * @param codesource the {@link CodeSource} to give the permissions to
     * @return a {@link PermissionCollection} whith all the appropriate permissions
     * @see SandboxSecurityPolicy
     */
    @Override
    protected PermissionCollection getPermissions(CodeSource codesource) {
        Permissions permissions = new Permissions();
        if (permissionProperties != null) {
            String pluginFileName = retrieveFileNameFromCodeSource(codesource);

            final String[] properties = new String[]{
                    "permissions." + pluginFileName + ".files",
                    "permissions." + pluginFileName + ".network",
                    "permissions." + pluginFileName + ".security",
                    "permissions." + pluginFileName + ".runtime"
            };

            String requiredPermissions;

            if (permissionProperties.isEmpty()) {
                return permissions;
            }

            for (String key : properties) {
                if (permissionProperties.containsKey(key)) {
                    requiredPermissions = permissionProperties.getProperty(key);
                    String[] slices = requiredPermissions.split(", ");

                    if (key.equals(properties[0])) {
                        for (String s : slices) {
                            String[] params = s.split(" ");
                            permissions.add(new FilePermission(params[1], params[0]));
                        }
                    } else if (key.equals(properties[1])) {
                        for (String s : slices) {
                            String[] params = s.split(" ");
                            permissions.add(new NetPermission(params[0]));
                        }
                    } else if (key.equals(properties[2])) {
                        for (String s : slices) {
                            String[] params = s.split(" ");
                            permissions.add(new SecurityPermission(params[0]));
                        }
                    } else if (key.equals(properties[3])) {
                        for (String s : slices) {
                            String[] params = s.split(" ");
                            permissions.add(new RuntimePermission(params[0]));
                        }
                    } else {
                        LOGGER.warn("{} is not a valid permission, skipping...");
                    }
                }
            }
        }
        return permissions;
    }

    /**
     * Retrieve the Jar file name without the .jar extension
     *
     * @param codeSource the {@link CodeSource} from which to retrieve the file name
     * @return the file name without the .jar extension
     */
    private String retrieveFileNameFromCodeSource(CodeSource codeSource) {
        JarURLConnection connection = null;
        try {
            connection = (JarURLConnection) codeSource.getLocation().openConnection();

        } catch (IOException e) {
            LOGGER.error("Can't open a connection to the codesource", e);
        }

        if (connection == null) {
            throw new IllegalStateException("Can't open a connection to the codesource");
        }

        String file;
        try {
            file = getName(connection.getJarFile().getName());
        } catch (IOException e) {
            throw new IllegalStateException("Error reading JarFile: " + e.getMessage(), e);
        }
        if (file == null) {
            throw new IllegalStateException("Error reading JarFile");
        }
        return file.substring(0, file.length() - 4);
    }

    public Properties getPermissionProperties() {
        return permissionProperties;
    }

    public void setPermissionProperties(Properties permissionProperties) {
        this.permissionProperties = permissionProperties;
    }

    /**
     * Get the specific file name from a full path file name.
     *
     * @param filename the full path file name
     * @return the name of the file
     */
    private static String getName(String filename) {
        if (filename == null) {
            return null;
        }
        int index = indexOfLastSeparator(filename);
        return filename.substring(index + 1);
    }

    /**
     * Operate on the full path file name by OS separation character
     *
     * @param filename the full path file name
     * @return the position of the last element of the path (aka the simple file name)
     */
    private static int indexOfLastSeparator(String filename) {
        if (filename == null) {
            return -1;
        }
        int lastUnixPos = filename.lastIndexOf(UNIX_SEPARATOR);
        int lastWindowsPos = filename.lastIndexOf(WINDOWS_SEPARATOR);
        return Math.max(lastUnixPos, lastWindowsPos);
    }
}
