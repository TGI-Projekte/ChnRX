/*
 * Copyright (C) 2018 Zisis Relas, Florian Rost, Julian Blazek
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
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package net.nailuj.chnrx;

import java.io.File;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.UIManager;

/**
 * ChnRX.java Zweck: Startet das Spiel und setzt das Systemspezifische Swing
 * Look-and-feel.
 */
public class ChnRX {

    private static Steuerung steu;
    public static final String version = "v1.0.0";

    public ChnRX() {
        // Setzt das LaF auf das System LaF..
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());

        } catch (Exception e) {
            e.printStackTrace();
        }
        steu = new Steuerung();
    }

    /**
     * Startet das Spiel neu durch löschen und neuerzeugen des Steuerung
     * Objektes. Zudem wird der JVM Garbage Collector aufgerufen.
     */
    public static void restart() {
        System.out.println("Spiel wird neugestartet..");
        steu = null;
        try {
            restartApplication(null);
        } catch (IOException ex) {
            Logger.getLogger(ChnRX.class.getName()).log(Level.SEVERE, null, ex);
            System.exit(0);
        }
    }

    public static void main(String[] args) {
        new ChnRX();
    }

    /**
     * Code stolen from: http://lewisleo.blogspot.de/2012/08/programmatically-restart-java.html
     * @param runBeforeRestart
     * @throws IOException 
     */
    public static void restartApplication(Runnable runBeforeRestart) throws IOException {
    try {
        // java binary
        String java = System.getProperty("java.home") + "/bin/java";
        // vm arguments
        List<String> vmArguments = ManagementFactory.getRuntimeMXBean().getInputArguments();
        StringBuffer vmArgsOneLine = new StringBuffer();
        for (String arg : vmArguments) {
            // if it's the agent argument : we ignore it otherwise the
            // address of the old application and the new one will be in conflict
            if (!arg.contains("-agentlib")) {
                vmArgsOneLine.append(arg);
                vmArgsOneLine.append(" ");
            }
        }
        // init the command to execute, add the vm args
        final StringBuffer cmd = new StringBuffer("\"" + java + "\" " + vmArgsOneLine);
        // program main and program arguments (be careful a sun property. might not be supported by all JVM) 
        String[] mainCommand = System.getProperty("sun.java.command").split(" ");
        // program main is a jar
        if (mainCommand[0].endsWith(".jar")) {
            // if it's a jar, add -jar mainJar
            cmd.append("-jar " + new File(mainCommand[0]).getPath());
        } else {
            // else it's a .class, add the classpath and mainClass
            cmd.append("-cp \"" + System.getProperty("java.class.path") + "\" " + mainCommand[0]);
        }
        // finally add program arguments
        for (int i = 1; i < mainCommand.length; i++) {
            cmd.append(" ");
            cmd.append(mainCommand[i]);
        }
        // execute the command in a shutdown hook, to be sure that all the
        // resources have been disposed before restarting the application
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                try {
                    Runtime.getRuntime().exec(cmd.toString());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        // execute some custom code before restarting
        if (runBeforeRestart != null) {
            runBeforeRestart.run();
        }
        // exit
        System.exit(0);
    } catch (Exception e) {
        // something went wrong
        throw new IOException("Error while trying to restart the application", e);
    }
}
}
