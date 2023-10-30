import java.awt.*;
import java.util.LinkedList;
import java.util.Random;
import javax.swing.*;
import java.util.concurrent.atomic.AtomicInteger;

public class Main {
    public static JProgressBar progressBar;
    public static JButton addWorkerButton;
    public static JButton removeWorkerButton;
    public static LinkedList<Item> buffer = new LinkedList<>();
    public static AtomicInteger workerCount = new AtomicInteger(0); // Rename workerCount

    public static void main(String[] args) {
        JFrame frame = new JFrame("Producer Consumer");
        frame.setVisible(true);
        frame.setSize(500, 500);
        frame.setLayout(new FlowLayout());

        progressBar = new JProgressBar(0, 100);
        progressBar.setStringPainted(true);
        frame.add(progressBar);

        addWorkerButton = new JButton("Add Worker");
        frame.add(addWorkerButton);

        removeWorkerButton = new JButton("Remove Worker");
        frame.add(removeWorkerButton);

        JLabel workerCountLabel = new JLabel("Workers: " + workerCount.get()); // Rename label
        frame.add(workerCountLabel);

        addWorkerButton.addActionListener(e -> {
            if (workerCount.get() < 15) {
                workerCount.incrementAndGet();
                workerCountLabel.setText("Workers: " + workerCount.get()); // Update worker count label
                new Thread(new Producer(buffer, progressBar)).start();
            }
        });

        removeWorkerButton.addActionListener(e -> {
            if (workerCount.get() > 1) {
                workerCount.decrementAndGet();
                workerCountLabel.setText("Workers: " + workerCount.get()); // Update worker count label
            }
        });

        int numConsumers = new Random().nextInt(13) + 3;
        for (int i = 0; i < numConsumers; i++) {
            int consumptionTime = new Random().nextInt(10) + 1;
            new Thread(new Consumer(buffer, progressBar, consumptionTime)).start();
        }
    }
}
