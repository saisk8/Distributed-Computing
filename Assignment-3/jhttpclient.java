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
        System.out.print("Enter the file you want to GET: ");
        request += inFromUser.readLine();
        request += " HTTP/1.1";
        outToServer.writeBytes(request + "\n\n");
        System.out.println("Reply from server(the file contents):\n");
        while ((response = inFromServer.readLine()) != null) {
            System.out.println(response);
        }
        clientSocket.close();
    }
}