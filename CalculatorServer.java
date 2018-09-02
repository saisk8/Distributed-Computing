import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Support add, subtraction, multiplication, division
 * 
 * @author hluu
 *
 */
public class CalculatorServer {
    enum OPERATOR {
        ADD, SUB, MULT, DIV
    };

    public static final int PORT_NO = 3000;

    /**
     * @param args
     * @throws IOException
     * @throws InterruptedException
     */
    public static void main(String[] args) throws IOException, InterruptedException {

        ServerSocket serverSocket = new ServerSocket(PORT_NO);
        System.out.println("... Server is accepting request now");

        while (true) {
            Socket socket = serverSocket.accept();

            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String line = reader.readLine();

            System.out.println("Request: " + line);

            if (line.trim().startsWith("quit")) {
                System.out.println("Server shutting down ...");
                socket.close();
                break;
            } else {
                System.out.println(line);
                processRequest(socket, line);
            }
        }

        System.out.println("... closing server socket ...");
        serverSocket.close();
    }

    private static void processRequest(Socket socket, String line) throws IOException, InterruptedException {
        PrintWriter pw = new PrintWriter(socket.getOutputStream());

        String[] tokens = line.split(" ");

        if (tokens.length != 2) {
            pw.println("invalid command: " + line);
            socket.close();
            return;
        }

        String[] operands = tokens[1].split(",");

        if (operands.length != 2) {
            pw.println("invalid command: " + line);
            socket.close();
            return;
        }

        String operator = tokens[0].trim();

        try {
            Double operand1 = Double.valueOf(operands[0].trim());
            Double operand2 = Double.valueOf(operands[1].trim());
            System.out.println(operand1);
            System.out.println(operand2);

            double result = 0;
            OPERATOR op = OPERATOR.valueOf(operator.toUpperCase());
            switch (op) {
            case ADD:
                result = operand1 + operand2;
                break;
            case SUB:
                System.out.println("Entering SUB");
                result = operand1 - operand2;
                break;
            case MULT:
                result = operand1 * operand2;
                break;
            case DIV:
                result = operand1 / operand2;
                break;
            default:
                pw.println("invalid operand: " + line);
                pw.flush();
                socket.close();
                return;
            }

            System.out.println("send back result: " + result);
            pw.println(result);
        } catch (NumberFormatException nfe) {
            pw.println("invalid operand: " + line);
        }

        pw.flush();
        socket.close();
    }

}