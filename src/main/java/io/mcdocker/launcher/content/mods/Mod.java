/*
 * MCDocker, an open source Minecraft launcher.
 * Copyright (C) 2021 MCDocker
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package io.mcdocker.launcher.content.mods;

import java.lang.reflect.InvocationTargetException;

public abstract class Mod<T extends ModManifest> {

    protected final T manifest;

    public Mod(T manifest) {
        this.manifest = manifest;
    }

    public String getName() {
        return manifest.getName();
    }

    public T getManifest() {
        return manifest;
    }

    public static Mod<?> of(ModManifest manifest) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException, ClassNotFoundException {
        return (Mod<?>) Class.forName(manifest.getType()).getConstructor(manifest.getClass()).newInstance(manifest);
    }

}
