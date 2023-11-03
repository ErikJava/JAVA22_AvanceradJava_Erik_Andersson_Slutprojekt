package gui;

import controllers.Controller;
import models.Producer;
import models.Consumer;
import services.Buffer;
import utils.Log;

import java.awt.*;
import javax.swing.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class Main {
    public static JProgressBar progressBar;
    public static JButton addWorkerButton;
    public static JButton removeWorkerButton;
    public static Buffer buffer = new Buffer();
    public static AtomicInteger totalWorkDone = new AtomicInteger(0);
    public static AtomicInteger totalProducedItems = new AtomicInteger(0);
    public static List<Thread> producerThreads = new ArrayList<>();
    public static JTextArea logTextArea;
    public static long startTimeMillis = System.currentTimeMillis();

    public static void main(String[] args) {
        JFrame frame = new JFrame("models.Producer models.Consumer");
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

        JScrollPane logScrollPane = new JScrollPane(logTextArea);
        frame.add(logScrollPane);


        Log logger = Log.getInstance();


        logger.setLogTextArea(logTextArea);


        progressBar.addChangeListener(e -> {
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
        });

        addWorkerButton.addActionListener(e -> {
            if (producerThreads.size() < 25) {
                int productionTime = generateRandomProductionTime(1, 10);
                Thread producerThread = new Thread(new Producer(buffer, progressBar, totalWorkDone, totalProducedItems, productionTime));
                producerThreads.add(producerThread);
                producerThread.start();
                workerCountLabel.setText("Workers: " + producerThreads.size());


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


                String logMessage = "Worker removed. Total workers: " + producerThreads.size();
                logger.log(logMessage);
            }
        });

        int numConsumers = new Random().nextInt(13) + 3;
        for (int i = 0; i < numConsumers; i++) {
            new Thread(new Consumer(buffer, progressBar)).start();
        }

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);


        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
        scheduler.scheduleAtFixedRate(() -> logger.logAverageProducedItems(startTimeMillis, totalProducedItems), 0, 10, TimeUnit.SECONDS);


        Controller.initializeConsumer(buffer, progressBar);
    }


    private static int generateRandomProductionTime(int min, int max) {
        Random random = new Random();
        return random.nextInt(max - min + 1) + min;
    }
}







