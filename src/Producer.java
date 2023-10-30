import javax.swing.*;
import java.util.LinkedList;
import java.util.Random;

public class Producer implements Runnable {
    public final LinkedList<Item> buffer;


    public Producer(LinkedList<Item> buffer, JProgressBar progressBar) {
        this.buffer = buffer;
    }

    @Override
    public void run() {
        while (true) {
            try {
                Thread.sleep((new Random().nextInt(2) + 1) * 1000);
                synchronized (buffer) {
                    buffer.add(new Item("Produced: " + (char) (new Random().nextInt(100))));
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
            Main.progressBar.setValue(Math.min(value + 2, 100));
        });
    }
}