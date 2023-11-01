import javax.swing.*;
import java.util.LinkedList;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

public class Producer implements Runnable {
    public final LinkedList<Item> buffer;
    public final JProgressBar progressBar;
    public final AtomicInteger totalWorkDone;
    public final int productionTime;

    public Producer(LinkedList<Item> buffer, JProgressBar progressBar, AtomicInteger totalWorkDone, int productionTime) {
        this.buffer = buffer;
        this.progressBar = progressBar;
        this.totalWorkDone = totalWorkDone;
        this.productionTime = productionTime;
    }

    @Override
    public void run() {
        while (true) {
            try {
                Thread.sleep(productionTime * 1000L);
                synchronized (buffer) {
                    String items = "" + (char) (new Random().nextInt(100));
                    buffer.add(new Item(items));
                   // String logMessage = "Produced: " + message;
                   // Log.log(logMessage); // Log the message
                }
                updateProgressBar();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    public void updateProgressBar() {
        SwingUtilities.invokeLater(() -> {
            int value = Main.progressBar.getValue();
            Main.progressBar.setValue(Math.min(value + 1, 100));
        });
    }
}

