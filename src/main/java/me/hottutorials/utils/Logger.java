package me.hottutorials.utils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Logger {

    private static String date = new SimpleDateFormat("dd-MM-yy").format(new Date().getTime());
    private final static File logFile = new File(OSUtils.getUserData() + "logs/log-" + date + ".log");

    public static void log(Object log) {
        try {
            logToFile(true, log);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void err(Object err) {
        try {
            logToFile(false, err);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void logToFile(boolean normal, Object message) throws IOException {
        if(!logFile.exists()) {
            logFile.getParentFile().mkdir();
            logFile.createNewFile();
        }


        FileWriter writer = new FileWriter(logFile, true);
        String prefix = "[ " + new SimpleDateFormat("HH:mm:ss").format(new Date().getTime()) + " ]";

        String msg = prefix + (normal ? " " : " Error: ") + message;

        if(normal) System.out.println(msg);
        else System.err.println(msg);

        writer.write(msg + "\r\n");
        writer.flush();
        writer.close();
    }

}
