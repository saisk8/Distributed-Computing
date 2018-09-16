import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.UnknownHostException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketTimeoutException;

public class CalculatorClient {
// Sever port number
public static final int SERVER_PORT_NUMBER = 3000;
// Resend timeout (milliseconds)
private static final int TIMEOUT = 3000;
// Maximum retransmissions
private static final int MAXTRIES = 5;

// String to help the user undertsand the usage...
public static final String INFO = "This is a client that is able to connect to a server running on localhost at port 3000.\n"
                                  + "The client is able to request the server to perform basic arithematic operations. \n"
                                  + "The way to request those operations is as follows: \n" + "1. Addition: ADD operand1 operand2 \n"
                                  + "2. Substraction: SUB operand1 operand2 \n" + "3. Multiplication: MUL operand1 operand2 \n"
                                  + "4. Division: DIV operand1 operand2 \n"
                                  + "The operation performed is: operand 1 [operation(+|-|*|/)] opearnd 2\n"
                                  + "An example valid request: SUB: 17.6 9.6\n To stop, send 'quit' as the request\n"
                                  + "\n\n\nEnter your request: ";

public static void main(String[] args) throws UnknownHostException, IOException {
        // Print info
        System.out.println(INFO);
        // Get operation and operands
        BufferedReader inputFromUser = new BufferedReader(new InputStreamReader(System.in));
        String operation = inputFromUser.readLine();

        // Create a client socket
        DatagramSocket clientSocket = new DatagramSocket();
        // Maximum receive blocking time (milliseconds)
        clientSocket.setSoTimeout(TIMEOUT);
        InetAddress IPAddress = InetAddress.getByName("localhost");

        // Create a DatagramPacket object and feed it with the server's IP and Port
        byte[] sendData = new byte[1024];
        byte[] receiveData = new byte[1024];
        sendData = operation.getBytes();
        DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, SERVER_PORT_NUMBER);
        DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
        int tries = 0;      // Packets may be lost, so we have to keep trying
        boolean receivedResponse = false;
        do {
                clientSocket.send(sendPacket);
                try {
                        // Wait for reception of service and display the result to the user
                        // Receive response
                        clientSocket.receive(receivePacket);
                        receivedResponse = true;
                } catch(SocketTimeoutException e) {
                        System.out.println("Time out, " + (MAXTRIES - tries) + " more tries...");
                        tries++;
                }
        } while ((!receivedResponse) && (tries < MAXTRIES));


        if (receivedResponse) {
                System.out.println("Reply from server: \n" + new String(receivePacket.getData()));
        } else {
                System.out.println("No data received from the server");
        }


        // Close socket
        clientSocket.close();
}
}
