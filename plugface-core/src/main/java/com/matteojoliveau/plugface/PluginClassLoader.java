package com.matteojoliveau.plugface;

import sun.net.www.protocol.jar.URLJarFile;

import java.io.*;
import java.net.NetPermission;
import java.net.URL;
import java.net.URLClassLoader;
import java.net.URLConnection;
import java.security.CodeSource;
import java.security.PermissionCollection;
import java.security.Permissions;
import java.util.*;

public class PluginClassLoader extends URLClassLoader {
    private File permissionsFile;

    public PluginClassLoader(URL jarFileUrl) {
        super(new URL[]{jarFileUrl});
    }

    public PluginClassLoader(URL[] jarFileUrls) {
        super(jarFileUrls);
    }

    @Override
    protected PermissionCollection getPermissions(CodeSource codesource) {
        Permissions permissions = new Permissions();
        String pluginFileName = retrieveFileNameFromCodeSource(codesource);

        final String[] properties = new String[]{
                "permissions." + pluginFileName + ".files",
                "permissions." + pluginFileName + ".network"
        };

        Properties prop = new Properties();
        String requiredPermissions = null;

//        InputStream input = ClassLoader.getSystemClassLoader().getResourceAsStream("permissions.properties");
        InputStream input = null;
        try {
            input = new FileInputStream(permissionsFile);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        try {
            prop.load(input);
        } catch (IOException e) {
            e.printStackTrace();
        }

        for (String key : properties) {
            if (prop.containsKey(key)) {
                requiredPermissions = prop.getProperty(key);
                String[] slices = requiredPermissions.split(", ");

                if (key.equals(properties[0])) {
                    for (String s : slices) {
                        String[] params = s.split(" ");
                        permissions.add(new FilePermission(params[1], params[0]));
                    }
                } else if (key.equals(properties[1])) {
                    for (String s : slices) {
                        String[] params = s.split(" ");
                        permissions.add(new NetPermission(params[1]));
                    }
                }


            }
        }


        return permissions;
    }

    private String retrieveFileNameFromCodeSource(CodeSource codeSource) {
        URLConnection connection = null;
        try {
            connection = codeSource.getLocation().openConnection();
        } catch (IOException e) {
            e.printStackTrace();
        }

        File file = null;
        try {
            file = new File(((URLJarFile) connection.getContent()).getName());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file.getName().substring(0, file.getName().length() - 4);
    }

    public File getPermissionsFile() {
        return permissionsFile;
    }

    public void setPermissionsFile(File permissionsFile) {
        this.permissionsFile = permissionsFile;
    }
}