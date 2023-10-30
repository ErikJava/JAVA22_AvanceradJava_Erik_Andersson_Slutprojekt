import javax.swing.*;
import java.util.LinkedList;

public class Consumer implements Runnable {
    public final LinkedList<Item> buffer;
    public final JProgressBar progressBar;
    public final int consumptionTime;

    public Consumer(LinkedList<Item> buffer, JProgressBar progressBar) {
        this(buffer, progressBar, 4); // Default consumption time (5 seconds)
    }

    public Consumer(LinkedList<Item> buffer, JProgressBar progressBar, int consumptionTime) {
        this.buffer = buffer;
        this.progressBar = progressBar;
        this.consumptionTime = consumptionTime;
    }

    @Override
    public void run() {
        while (true) {
            try {
                Thread.sleep(consumptionTime * 1000);
                Item item;
                synchronized (buffer) {
                    if (!buffer.isEmpty()) {
                        item = buffer.removeFirst();
                    } else {
                        continue;
                    }
                }
                System.out.println("Consumed: " + item.getMessage());
                updateProgressBar();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    public void updateProgressBar() {
        SwingUtilities.invokeLater(() -> {
            int value = Main.progressBar.getValue();
            Main.progressBar.setValue(Math.min(value - 2, 100));
        });
    }
}


