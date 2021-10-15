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

package io.mcdocker.launcher.utils;

import io.mcdocker.launcher.MCDocker;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Logger {

    private static String date = new SimpleDateFormat("dd-MM-yy").format(new Date().getTime());
    private final static File logFile = new File(new File(Folders.USER_DATA, "logs"), "log-" + date + ".log");

    public static void log(Object log) {
        try {
            logToFile(" ", log);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void err(Object err) {
        try {
            logToFile(" Error: ", err);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void debug(Object log) {
        try {
            if(MCDocker.getArgument("debugMode").equalsIgnoreCase("true")) {
                logToFile(" Debug: ", log);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void logToFile(String type, Object message) throws IOException {
        if(!logFile.exists()) {
            logFile.getParentFile().mkdir();
            logFile.createNewFile();
        }


        FileWriter writer = new FileWriter(logFile, true);
        String prefix = "[ " + new SimpleDateFormat("HH:mm:ss").format(new Date().getTime()) + " ]";

        String msg = prefix + type + message;

        System.out.println(msg);

        writer.write(msg + "\r\n");
        writer.flush();
        writer.close();
    }

}
