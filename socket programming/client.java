
import java.net.*;
import java.io.*;
import java.util.Scanner;

public class client {

    public static void main(String[] args) {
        String serverName = args[0];
        int port = Integer.parseInt(args[1]);
        // String serverName = "localhost";
        // int port = 4999;
        try {
            System.out.println("Connecting to " + serverName + " on port " + port);
            Socket client = new Socket(serverName, port);

            System.out.println("Just connected to " + client.getRemoteSocketAddress());
            OutputStream outToServer = client.getOutputStream();
            DataOutputStream out = new DataOutputStream(outToServer);

            System.out.println("Enter message: \n");
            Scanner sc = new Scanner(System.in);
            String tosend = sc.nextLine();
            out.writeUTF(tosend);

            // out.writeUTF("Hello from " + client.getLocalSocketAddress());
            InputStream inFromServer = client.getInputStream();
            DataInputStream in = new DataInputStream(inFromServer);

            System.out.println("Server says " + in.readUTF());

            client.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}