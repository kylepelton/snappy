package fxapp;

import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.Date;

public class Logger {

    final static String delim = System.getProperty("file.separator");

    private Logger() {
        // Don't construct anything
    }

    public static void log(String text) {
        try {
            File logFile = new File(System.getProperty("user.home") + delim
                + ".snappy" + delim + "logs" + delim + "LOG.txt");
            if (!logFile.exists()) {
                // This shouldn't ever happen
            } else {
                FileWriter fw = null;
                BufferedWriter bw = null;
                PrintWriter pw = null;
                fw = new FileWriter(logFile, true);
                bw = new BufferedWriter(fw);
                pw = new PrintWriter(bw);
                if (pw != null) {
                    Date currDate = new Date(System.currentTimeMillis());
                    pw.println("\n" + currDate.toString());
                    pw.println(text + "\n");
                    pw.close();
                }
            }
        } catch (Exception logException) {
            // At this point, I don't think anything can be done
        }
    }

    public static void log(Exception e) {
        try {
            File logFile = new File(System.getProperty("user.home") + delim
                + ".snappy" + delim + "logs" + delim + "LOG.txt");
            if (!logFile.exists()) {
                // This shouldn't ever happen
            } else {
                FileWriter fw = null;
                BufferedWriter bw = null;
                PrintWriter pw = null;
                String trace = exceptionStacktraceToString(e);
                fw = new FileWriter(logFile, true);
                bw = new BufferedWriter(fw);
                pw = new PrintWriter(bw);
                if (pw != null) {
                    Date currDate = new Date(System.currentTimeMillis());
                    pw.println("\n" + currDate.toString());
                    pw.println(trace + "\n");
                    pw.close();
                }
            }
        } catch (Exception logException) {
            // At this point, I don't think anything can be done
        }
    }

    private static String exceptionStacktraceToString(Exception e) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos);
        e.printStackTrace(ps);
        ps.close();
        return baos.toString();
    }

}
