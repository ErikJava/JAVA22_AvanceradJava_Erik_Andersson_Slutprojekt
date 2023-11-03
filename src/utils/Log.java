package utils;

import javax.swing.*;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.concurrent.atomic.AtomicInteger;

public class Log {
    private static final String LOG_FILE = "log.txt";
    private JTextArea logTextArea;
    private static Log instance = null;

    private Log() {
    }

    public static Log getInstance() {
        if (instance == null) {
            instance = new Log();
        }
        return instance;
    }

    public void setLogTextArea(JTextArea textArea) {
        logTextArea = textArea;
    }

    public void log(String message) {
        try (OutputStream os = new BufferedOutputStream(new FileOutputStream(LOG_FILE, true));
             Writer writer = new OutputStreamWriter(os, StandardCharsets.UTF_8)) {

            Date timestamp = new Date();
            String logMessage = timestamp + ": " + message;
            writer.write(logMessage);
            writer.write(System.lineSeparator());
            writer.flush();

            if (logTextArea != null) {
                appendToLogTextArea(logMessage);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Method for logging the average produced items
    public void logAverageProducedItems(long startTimeMillis, AtomicInteger totalProducedItems) {
        long currentTimeMillis = System.currentTimeMillis();
        long elapsedTimeSeconds = (currentTimeMillis - startTimeMillis) / 1000;
        int totalProduced = totalProducedItems.get();

        if (elapsedTimeSeconds >= 10) {
            int averageProduced = (int) (totalProduced / (elapsedTimeSeconds / 10));
            String logMessage = "Average Produced Items (over 10 seconds): " + averageProduced;
            log(logMessage);
        }
    }

    // Method for logging the average consumed items
    private void appendToLogTextArea(String message) {
        SwingUtilities.invokeLater(() -> {
            if (logTextArea != null) {
                logTextArea.insert(message + System.lineSeparator(), 0);
                logTextArea.setCaretPosition(0);
            }
        });
    }

}

