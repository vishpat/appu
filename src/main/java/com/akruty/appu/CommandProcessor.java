package com.akruty.appu;


import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * Created by vpati011 on 9/4/17.
 */
public class CommandProcessor {

    private static final String commandFile = ".appu.commands";

    private static Properties commands = new Properties();

    public static boolean loadCommands()
    {
        String commandFilePath = System.getProperty("user.home") + File.separator + commandFile;
        try (FileInputStream in = new FileInputStream(commandFilePath)) {
            commands.load(in);
        } catch (IOException ex) {
            System.err.println("Unable to load command file " + commandFilePath + " " + ex.getMessage());
            return false;
        }

        return true;
    }

    public void runCommand(String command) {
        if (commands == null) {
            System.err.println("Command file not loaded");
            return;
        }

        String cmd = commands.getProperty(command.toLowerCase());
        if (cmd != null) {
            try {
                Process p = Runtime.getRuntime().exec(cmd);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            System.err.println(command + " not found");
        }
    }
}
