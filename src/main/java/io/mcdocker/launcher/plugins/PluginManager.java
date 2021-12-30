/*
 *
 *   MCDocker, an open source Minecraft launcher.
 *   Copyright (C) 2021 MCDocker
 *
 *   This program is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 *
 *   This program is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU General Public License for more details.
 *
 *   You should have received a copy of the GNU General Public License
 *   along with this program.  If not, see <https://www.gnu.org/licenses/>.
 *
 */

package io.mcdocker.launcher.plugins;

import io.mcdocker.launcher.utils.Folders;
import org.reflections.Reflections;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class PluginManager {

    private final List<Plugin> loadedPlugins = new ArrayList<>();
    private final File pluginsFolder = new File(Folders.USER_DATA, "plugins");

    private static final PluginManager instance = new PluginManager();
    public static PluginManager getManager() { return instance; }

    // TODO: Finish plugins

    public void registerPlugins() throws MalformedURLException {

        if(!pluginsFolder.exists()) pluginsFolder.mkdir();
        if(pluginsFolder.listFiles() == null) return;

        for (File file : pluginsFolder.listFiles()) {
            if (!file.isFile() && !file.getName().endsWith(".jar") && !file.canRead()) continue;

            URLClassLoader classLoader = new URLClassLoader(new URL[]{ file.toURI().toURL() }, this.getClass().getClassLoader());
            Set<Class<? extends Plugin>> pluginClasses = Reflections.collect().getSubTypesOf(Plugin.class);

            for (Class<? extends Plugin> pluginClass : pluginClasses) {
                try {
                    Method startMethod = pluginClass.getDeclaredMethod("start");
                    Method stopMethod = pluginClass.getDeclaredMethod("stop");
                    PluginProperties properties = pluginClass.getAnnotation(PluginProperties.class);
                    Object instance = pluginClass.newInstance();

                    startMethod.invoke(instance);
                } catch (NoSuchMethodException | InvocationTargetException | InstantiationException | IllegalAccessException e) {
                    e.printStackTrace();
                }
            }

        }
    }

}
