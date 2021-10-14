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

package io.mcdocker.launcher.content.clients;

import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

public class ClientDetails<CLIENT extends Client<?>> {

    private final String name;
    private final Supplier<CompletableFuture<CLIENT>> supplier;

    public ClientDetails(String name, Supplier<CompletableFuture<CLIENT>> supplier) {
        this.name = name;
        this.supplier = supplier;
    }

    public String getName() {
        return name;
    }

    public Supplier<CompletableFuture<CLIENT>> get() {
        return supplier;
    }

}
