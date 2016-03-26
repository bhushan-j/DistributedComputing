/**
 *  TEAM- E
 *  By Dr. Yi Zhao
 */

import java.io.*;
import java.net.*;
import java.util.*;
//the main class that opens a connection to the server, 
//starts a thread for communication, 
//and waits until the child thread finished.
public class SimpleJobClient {
    public static void main(String[] args) {
// Obtain host name & port number
        String hostName = "127.0.0.1";
        int portNumber = 9090;
        if (args.length >= 1)
            hostName = args[0];
        if (args.length >= 2)
            portNumber = Integer.parseInt(args[1]);

// Declare socket and thread
//the dedicated thread class that handles the communication 
//task with the server regarding the job request and submission.
        Socket socket = null;
        SimpleJobClientThread thread = null;

 // Open socket and start thread
        try {
            socket = new Socket(hostName, portNumber);
            thread = new SimpleJobClientThread(socket);
            thread.start();
            thread.join();
        } catch (UnknownHostException e) {
            System.err.println("Don't know about host.");
        } catch (IOException e) {
            System.err.println("Couldn't get I/O for the connection.");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

final class SimpleJobClientThread extends Thread {
    private Socket socket;

    public SimpleJobClientThread(Socket socket) {
        this.socket = socket;
    }

    public void run() {
        try {
            process();
        } catch (Exception e) {
            System.err.println(e);
        }
    }

    private void process() throws Exception {
 // Display connection
        String serverInfo = socket.getInetAddress() + ":" + socket.getPort();
        String clientInfo = socket.getLocalAddress() + ":" + socket.getLocalPort();
        System.out.println("Client " + clientInfo + " connected to Server " + serverInfo);
// Open input/output streams
        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
// Process job
        Random rand = new Random();
        double errorMargin = 0.4;
        boolean done = false;
        while (!done) {
            String inputLine;
            String outputLine;
// Request job
// Task1 : fill next line
            outputLine = "requestJob";
            System.out.println("client ->  " + outputLine);
            out.println(outputLine);
            sleep(2000);
// Process feedback
            inputLine = in.readLine();
            System.out.println("client <-  " + inputLine);
            String token[] = inputLine.split(" ");
// Task2: interpret response and take proper action
            int jobId=Integer.parseInt(token[1]);
            System.out.println("Processing encoding");
// String result="random";
            int randomNum = rand.nextInt((28 - 20) + 1) + 20;


            outputLine="submitJob"+" "+jobId+" "+randomNum;
            System.out.println("client ->  " + outputLine);
            out.println(outputLine);
            System.out.println(in.readLine());
            
        }
// Close streams and socket
        out.close();
        in.close();
        socket.close();
    }
}
