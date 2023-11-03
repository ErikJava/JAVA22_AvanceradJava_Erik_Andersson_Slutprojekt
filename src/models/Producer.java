package models;

import services.Buffer;
import utils.Item;

import javax.swing.*;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

public class Producer implements Runnable {
    public final Buffer buffer;
    public final JProgressBar progressBar;
    public final AtomicInteger totalWorkDone;
    public final AtomicInteger totalProducedItems;
    public final int productionTime;
    public volatile boolean keepProducing;

    public Producer(Buffer buffer, JProgressBar progressBar, AtomicInteger totalWorkDone, AtomicInteger totalProducedItems, int productionTime) {
        this.buffer = buffer;
        this.progressBar = progressBar;
        this.totalWorkDone = totalWorkDone;
        this.totalProducedItems = totalProducedItems;
        this.productionTime = productionTime;
        this.keepProducing = true;
    }

    @Override
    public void run() {
        while (keepProducing) {
            try {
                Thread.sleep(productionTime * 1000L);
                synchronized (buffer) {
                    if (!keepProducing) {
                        break;
                    }
                    String items = "" + (char) (new Random().nextInt(100));
                    buffer.add(new Item(items));
                    totalProducedItems.incrementAndGet();
                }
                if (keepProducing) {
                    updateProgressBar();
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    // Update the progress bar
    public void updateProgressBar() {
        SwingUtilities.invokeLater(() -> {
            int value = progressBar.getValue();
            int newValue = Math.min(value + 1, 100);
            progressBar.setValue(newValue);
        });
    }

    public void stopProducing() {
        keepProducing = false;
    }
}



