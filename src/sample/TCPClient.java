package sample;

import javafx.application.Platform;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.ByteOrder;
import java.nio.CharBuffer;
import java.util.concurrent.Semaphore;

/**
 * Created by tomericko on 14/01/16.
 */
public class TCPClient {

    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;

    public BufferedReader getStdin() {
        return stdin;
    }

    private BufferedReader stdin;
    private int port;
    private static TCPClient instance = null;


    public static boolean isConstruct() {
        return isConstruct;
    }

    private static boolean isConstruct = false;

    //constructor for the class TCPClient
        private TCPClient(String ip,int port){
        this.port = port;
        try{
            this.socket = new Socket(ip, port);
            this.out = new PrintWriter(socket.getOutputStream(), true);
            this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.stdin = new BufferedReader(new InputStreamReader(System.in));
            succesMsg msg = new succesMsg("Success", "You are connected");
            msg.show();
            isConstruct = true;
        }catch (IOException e){
            e.printStackTrace();
            errorMsg msg = new errorMsg("Error", "Can't connect, please try again.");
            msg.show();
            instance = null;
            isConstruct = false;
        }

    }

    public static TCPClient getInstance(){
        return instance;

    }

    public static TCPClient getInstance(String ip,int port){
        if(!isConstruct) {
            if(!isConstruct) {
                instance = new TCPClient(ip, port);
            }
        } else {
            errorMsg msg = new errorMsg("Error", "you are already connected!");
            msg.show();
        }
        return instance;

    }




    public String commandToServer(String com){
        StringBuilder result = new StringBuilder();
        String cur ;

        this.out.println(com + "$");
        try {
            boolean isFirst = true;
            while ((cur = in.readLine()) != null) {
                if (cur.toString().equals("~~-/START/-~~")) {
                    continue;
                } else if (cur.toString().equals("~~-/END/-~~")){
                    break;
                } else {
                        result.append(cur.toString());
                }
            }
        }catch (IOException e){
            e.printStackTrace();
        }
        return result.toString();

    }

}