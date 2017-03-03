package com.matteojoliveau.plugface;


import com.matteojoliveau.plugface.impl.DefaultPlugfaceContext;
import com.matteojoliveau.plugface.impl.DefaultPlugin;

import java.io.IOException;

public class PluginSystemIT {
    public static void main(String[] args) {
        PlugfaceContext context = new DefaultPlugfaceContext();
        Plugin test = new DefaultPlugin("testPlugin", context) {
            @Override
            public void start() {
                System.out.println("Starting");
            }

            @Override
            public void stop() {
                System.out.println("Stopping");
            }
        };

        PluginManager manager = new PluginManager("managerOne", context);

        manager.setPluginFolder("plugins");
        try {
            manager.loadPlugins();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }


    }

}
