import javax.swing.*;
import java.util.LinkedList;
import java.util.Random;

public class Consumer implements Runnable {
    public final LinkedList<Item> buffer;
    public final JProgressBar progressBar;

    public Consumer(LinkedList<Item> buffer, JProgressBar progressBar) {
        this.buffer = buffer;
        this.progressBar = progressBar;
    }

    @Override
    public void run() {
        int consumptionTime = generateRandomConsumptionTime(1, 10); // Random consumption time (1-10 seconds)
        consumeItem(consumptionTime);
    }

    private void consumeItem(int consumptionTime) {
        while (true) {
            try {
                Thread.sleep(consumptionTime * 1000L);
                Item item;
                synchronized (buffer) {
                    if (!buffer.isEmpty()) {
                        item = buffer.removeFirst();
                        //String logMessage = "Consumed: " + item.getMessage();
                       // Log.log(logMessage); // Log the message
                        updateProgressBar();
                    }
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    public void updateProgressBar() {
        SwingUtilities.invokeLater(() -> {
            int value = Main.progressBar.getValue();
            Main.totalWorkDone.decrementAndGet(); // Decrement totalWorkDone
            Main.progressBar.setValue(Math.max(value - 1, 0)); // Adjust the progress bar
        });
    }

    // Helper method to generate a random consumption time between min and max (inclusive)
    private int generateRandomConsumptionTime(int min, int max) {
        Random random = new Random();
        return random.nextInt(max - min + 1) + min;
    }
}




