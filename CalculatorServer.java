import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.DatagramPacket;
import java.io.BufferedReader;
import java.net.DatagramSocket;

public class CalculatorServer {
    enum OPERATOR {
        ADD, SUB, MUL, DIV
    };

    // Server port number
    public static final int SERVER_PORT_NO = 3000;

    public static void main(String[] args) throws IOException, InterruptedException {
        // Create server socket
        DatagramSocket serverSocket = new DatagramSocket(SERVER_PORT_NO);
        System.out.println("Server listening at " + SERVER_PORT_NO);
        byte[] receiveData = new byte[1024];
        byte[] sendData = new byte[1024];

        // An iterative server alive as long as 'quit' is not received from the
        // client...
        while (true) {
            DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
            serverSocket.receive(receivePacket);
            String request = new String(receivePacket.getData());
            System.out.println("RECEIVED: " + request);
            InetAddress IPAddress = receivePacket.getAddress();
            int port = receivePacket.getPort();
            // If quit is recieved shutdown the server...
            if (request.trim().startsWith("quit")) {
                System.out.println("Server shutting down ...");
                byte[] bye = new String("Good bye!").getBytes();
                DatagramPacket goodByePacket = new DatagramPacket(bye, bye.length, IPAddress, port);
                serverSocket.send(goodByePacket);
                serverSocket.close();
                break;
            } else {
                // Process the request from the client
                sendData = processRequest(serverSocket, request).getBytes();
            }

            // Send the computed result back to the client
            DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, port);
            serverSocket.send(sendPacket);
            System.out.println("Result computed and sent");
        }
    }

    private static String processRequest(DatagramSocket socket, String request)
            throws IOException, InterruptedException {
        // Split the request string in tokens using the split method...
        String[] tokens = request.split(" ");

        // Seperate operand 1 and operand 2
        String[] operands = tokens[1].split(",");

        // Check the validity of the request
        if (tokens.length != 2 || operands.length != 2) {
            socket.close();
            return "Error: Invalid command";
        }

        String operator = tokens[0].trim();

        // Perform the necessary computation
        try {
            Double operand1 = Double.valueOf(operands[0].trim());
            Double operand2 = Double.valueOf(operands[1].trim());

            double result = 0;
            OPERATOR op = OPERATOR.valueOf(operator.toUpperCase());
            switch (op) {
            case ADD:
                System.out.println("Operation to be performed: " + operand1 + " + " + operand2);
                result = operand1 + operand2;
                break;
            case SUB:
                System.out.println("Operation to be performed: " + operand1 + " - " + operand2);
                result = operand1 - operand2;
                break;
            case MUL:
                System.out.println("Operation to be performed: " + operand1 + " * " + operand2);
                result = operand1 * operand2;
                break;
            case DIV:
                System.out.println("Operation to be performed: " + operand1 + " / " + operand2);
                result = operand1 / operand2;
                break;
            default:
                System.out.println("Invalid Operation: " + request);
                break;
            }
            return Double.toString(result);
        } catch (NumberFormatException nfe) {
            System.out.println("Invalid operation: " + request);
        }
        return "Request could not be processed";
    }
}