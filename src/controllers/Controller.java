package controllers;

import models.Consumer;
import services.Buffer;

import javax.swing.*;

public class Controller {
    public static void initializeConsumer(Buffer buffer, JProgressBar progressBar) {
        Consumer consumer = new Consumer(buffer, progressBar);
        Thread consumerThread = new Thread(consumer);
        consumerThread.start();
    }
}


