import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.UnknownHostException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class CalculatorClient {
    // Sever port number
    public static final int SERVER_PORT_NUMBER = 3000;

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
        InetAddress IPAddress = InetAddress.getByName("localhost");

        // Creta a DatagramPacket object and feed it with the server's IP and Port
        byte[] sendData = new byte[1024];
        byte[] receiveData = new byte[1024];
        sendData = operation.getBytes();
        DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, SERVER_PORT_NUMBER);

        // Send request
        clientSocket.send(sendPacket);

        // Wait for reception of service and display the result to the user
        DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
        if (!"quit".equals(operation.trim())) {
            clientSocket.receive(receivePacket);
            System.out.println("Reply from server: \n" + new String(receivePacket.getData()));
        }

        // Close socket
        clientSocket.close();
    }
}