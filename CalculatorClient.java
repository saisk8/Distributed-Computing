import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class CalculatorClient {

    /**
     * @param args
     * @throws IOException
     * @throws UnknownHostException
     */
    public static void main(String[] args) throws UnknownHostException, IOException {
        String operation = args[0] + " " + args[1];

        // Create a new socket object and names it socket
        // the constructor requires the name of the computer and the port number to
        // which you want to connect
        Socket socket = new Socket("localhost", CalculatorServer.PORT_NO);

        // gets the socket's input stream and opens a BufferedReader on it.
        BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

        // gets the socket's output stream and opens a PrintWriter on it
        PrintWriter pw = new PrintWriter(socket.getOutputStream());

        pw.println(operation);
        pw.flush();

        if (!"quit".equals(operation.trim())) {
            String line = reader.readLine();
            reader.close();
            System.out.println("Line 36 in Client");
            System.out.println("result: " + line);
            System.out.println("Line 38 in Client");
        }
        socket.close();

    }

}