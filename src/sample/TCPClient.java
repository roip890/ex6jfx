package sample;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.ByteOrder;
import java.nio.CharBuffer;

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
            //succesMsg msg = new succesMsg("Success", "You are connected");
            //msg.show();
            System.out.println("Connect:");
            System.out.println(this.port);
            isConstruct = true;
        }catch (IOException e){
            e.printStackTrace();
            errorMsg msg = new errorMsg("Error", "Can't connect, please try again.");
            msg.show();
            isConstruct = false;
        }

    }

    public static TCPClient getInstance(){
        return instance;

    }

    public static TCPClient getInstance(String ip,int port){
        if(!isConstruct){
            //lock
            if(!isConstruct) {
                    instance = new TCPClient(ip, port);
                    if (!isConstruct) {
                        instance = null;
                    }

            }
            //unlock
        } else {
            errorMsg msg = new errorMsg("Error", "you are already connected!");
            msg.show();
        }
        return instance;

    }


    public String commandToServer(String com){
        StringBuilder result = new StringBuilder();
        String cur ;
        this.out.println(com);
        try {
            while (in.ready() && ((cur = in.readLine()) != null) ) {
                result.append(cur.toString());
            }
        }catch (IOException e){
            e.printStackTrace();
        }

        return result.toString();

    }

    public Socket getSocket() {
        return socket;
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
    }

    public PrintWriter getOut() {
        return out;
    }

    public void setOut(PrintWriter out) {
        this.out = out;
    }

    public BufferedReader getIn() {
        return in;
    }

    public void setIn(BufferedReader in) {
        this.in = in;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }
}
