import java.util.LinkedList;
import java.util.Queue;

public class Buffer {
    Queue<Item> buffer = new LinkedList<Item>();

    public synchronized void add(Item item) {
        buffer.add(item);
        notify();
        System.out.println(buffer);
    }

    public synchronized Item remove() throws InterruptedException {
        while (buffer.isEmpty()) {
            wait();
        }
        return buffer.remove();
    }
}