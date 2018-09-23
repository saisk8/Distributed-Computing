import java.io.*;
import java.net.*;
import java.util.*;

public class jhttpclient {
    public static void main(String argv[]) throws Exception {
        String request;
        String response;
        BufferedReader inFromUser = new BufferedReader(new InputStreamReader(System.in));
        Socket clientSocket = new Socket("localhost", 3000);
        DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());
        BufferedReader inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        request = "GET /";
        request += inFromUser.readLine();
        outToServer.writeBytes(request + '\n');
        System.out.println("Reply from server:");
        while ((response = inFromServer.readLine()) != null) {
            System.out.println(response);
        }
        clientSocket.close();
    }
}