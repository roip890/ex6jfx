package sample;

import javafx.application.Platform;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by roi on 21/01/16.
 */
public class SendToServer extends Thread {
    //private final Lock mutex = new ReentrantLock(true);
    String command;
    String message = null;
    SendToServer(String command) {
        this.command = command;
    }

    String getMessage() {
        return this.message;
    }
    @Override
    public void run() {
            try {
                this.message = TCPClient.getInstance().commandToServer(command);
            } catch (Exception e) {
                Platform.runLater(new errorMsg("Error!", "Can't communicate server.\nPlease connect to server"));
            }
    }
}
