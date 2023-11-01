import java.awt.*;
import javax.swing.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class Main {
    public static JProgressBar progressBar;
    public static JButton addWorkerButton;
    public static JButton removeWorkerButton;
    public static LinkedList<Item> buffer = new LinkedList<>();
    public static AtomicInteger totalWorkDone = new AtomicInteger(0);
    public static List<Thread> producerThreads = new ArrayList<>(); // Maintain a list of producer threads
    public static JTextArea logTextArea;
    private static JScrollPane logScrollPane;

    public static void main(String[] args) {
        JFrame frame = new JFrame("Producer Consumer");
        frame.setSize(650, 650);
        frame.setLayout(new FlowLayout());

        progressBar = new JProgressBar(0, 100);
        progressBar.setStringPainted(true);
        frame.add(progressBar);

        addWorkerButton = new JButton("Add Worker");
        frame.add(addWorkerButton);

        removeWorkerButton = new JButton("Remove Worker");
        frame.add(removeWorkerButton);

        JLabel workerCountLabel = new JLabel("Workers: " + producerThreads.size());
        frame.add(workerCountLabel);

        logTextArea = new JTextArea(20, 60);
        logTextArea.setEditable(false);

        logScrollPane = new JScrollPane(logTextArea);
        frame.add(logScrollPane);

        // Get the Singleton instance of Log
        Log logger = Log.getInstance();

        // Set the logTextArea in the Singleton instance
        logger.setLogTextArea(logTextArea);

        // Change listener for the progress bar to update its color
        progressBar.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                int value = progressBar.getValue();
                if (value <= 10) {
                    progressBar.setForeground(Color.RED);
                    String logMessage = "Units too low. Value: " + value;
                    logger.log(logMessage);
                } else if (value >= 90) {
                    progressBar.setForeground(Color.GREEN);
                    String logMessage = "Units too high. Value: " + value;
                    logger.log(logMessage);
                } else {
                    progressBar.setForeground(Color.GRAY);
                }
            }
        });

        addWorkerButton.addActionListener(e -> {
            if (producerThreads.size() < 25) {
                int productionTime = generateRandomProductionTime(1, 10);
                Thread producerThread = new Thread(new Producer(buffer, progressBar, totalWorkDone, productionTime));
                producerThreads.add(producerThread);
                producerThread.start();
                workerCountLabel.setText("Workers: " + producerThreads.size());

                // Log the event
                String logMessage = "Worker added. Total workers: " + producerThreads.size() +
                        ", Production Interval: " + productionTime + " seconds";
                logger.log(logMessage);
            }
        });

        removeWorkerButton.addActionListener(e -> {
            if (!producerThreads.isEmpty()) {
                Thread producerThread = producerThreads.remove(producerThreads.size() - 1);
                producerThread.interrupt();
                workerCountLabel.setText("Workers: " + producerThreads.size());

                // Log the event
                String logMessage = "Worker removed. Total workers: " + producerThreads.size();
                logger.log(logMessage);
            }
        });

        int numConsumers = new Random().nextInt(13) + 3; // Random number of consumers between 3 and 15
        for (int i = 0; i < numConsumers; i++) {
            new Thread(new Consumer(buffer, progressBar)).start();
        }

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

    // Helper method to generate a random production time between min and max (inclusive)
    private static int generateRandomProductionTime(int min, int max) {
        Random random = new Random();
        return random.nextInt(max - min + 1) + min;
    }
}




