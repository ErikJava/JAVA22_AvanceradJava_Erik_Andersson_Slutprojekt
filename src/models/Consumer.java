package models;

import services.Buffer;
import utils.Item;

import javax.swing.*;
import java.util.Random;

import static gui.Main.totalWorkDone;

public class Consumer implements Runnable {
    public final Buffer buffer;
    public final JProgressBar progressBar;

    public Consumer(Buffer buffer, JProgressBar progressBar) {
        this.buffer = buffer;
        this.progressBar = progressBar;
    }

    @Override
    public void run() {
        int consumptionTime = generateRandomConsumptionTime(1, 10);
        consumeItem(consumptionTime);
    }

    // Consume an item from the buffer
    private void consumeItem(int consumptionTime) {
        while (true) {
            try {
                Thread.sleep(consumptionTime * 1000L);
                Item item = buffer.remove();
                updateProgressBar();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    // Update the progress bar
    public void updateProgressBar() {
        SwingUtilities.invokeLater(() -> {
            int value = progressBar.getValue();
            totalWorkDone.decrementAndGet();
            progressBar.setValue(Math.max(value - 1, 0));
        });
    }

    // Generate a random number between min and max
    private int generateRandomConsumptionTime(int min, int max) {
        Random random = new Random();
        return random.nextInt(max - min + 1) + min;
    }
}





