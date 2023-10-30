import java.util.LinkedList;

public class Controller {

    public static void main(String[] args) {
        LinkedList<Item> buffer = new LinkedList<>(); // Create a LinkedList<Item>

        Consumer consumer = new Consumer(buffer, Main.progressBar); // Pass a JProgressBar
        Thread consumerThread = new Thread(consumer);
        consumerThread.start();
    }
}