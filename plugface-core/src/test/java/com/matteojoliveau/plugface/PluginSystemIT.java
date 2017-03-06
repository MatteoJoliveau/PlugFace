package com.matteojoliveau.plugface;


import com.matteojoliveau.plugface.impl.DefaultPlugfaceContext;
import com.matteojoliveau.plugface.impl.DefaultPlugin;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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

        manager.setPluginFolder("C:\\Users\\Matteo\\IdeaProjects\\PlugFace\\plugface-core\\src\\main\\resources");
        List<Plugin> loaded = new ArrayList<>();
        try {
            loaded = manager.loadPlugins();
        } catch (IOException | ClassNotFoundException | InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }

        for (Plugin plugin :
                loaded) {
            context.addPlugin(plugin.getName(), plugin);
        }

        context.getPlugin("TestPlugin").start();



    }

}
