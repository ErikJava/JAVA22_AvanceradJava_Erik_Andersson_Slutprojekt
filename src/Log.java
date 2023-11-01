import javax.swing.*;
import java.io.*;
import java.util.Date;

public class Log {
    private static final String LOG_FILE = "log.txt";
    private JTextArea logTextArea; // Reference to the JTextArea
    private static Log instance = null; // Singleton instance

    // Private constructor to prevent instantiation from outside
    private Log() {
    }

    // Get the Singleton instance
    public static Log getInstance() {
        if (instance == null) {
            instance = new Log();
        }
        return instance;
    }

    // Initialize the logTextArea reference
    public void setLogTextArea(JTextArea textArea) {
        logTextArea = textArea;
    }

    public void log(String message) {
        try (OutputStream os = new BufferedOutputStream(new FileOutputStream(LOG_FILE, true));
             Writer writer = new OutputStreamWriter(os, "UTF-8")) {

            Date timestamp = new Date();
            String logMessage = timestamp + ": " + message;
            writer.write(logMessage);
            writer.write(System.lineSeparator());
            writer.flush(); // Ensure that the message is written immediately

            if (logTextArea != null) {
                appendToLogTextArea(logMessage);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void appendToLogTextArea(String message) {
        SwingUtilities.invokeLater(() -> {
            if (logTextArea != null) {
                logTextArea.append(message + System.lineSeparator()); // Append the message to the JTextArea
                logTextArea.setCaretPosition(logTextArea.getDocument().getLength()); // Scroll to the bottom
            }
        });
    }
}


