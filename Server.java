import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    static void main() {
        int port = 12005;

        try (ServerSocket serverSocket = new ServerSocket(port)) {
            while (true) {
                try (Socket clientSocket = serverSocket.accept()) {
                    BufferedReader in = new BufferedReader(
                            new InputStreamReader(clientSocket.getInputStream())
                    );

                    PrintWriter out = new PrintWriter(
                            clientSocket.getOutputStream(), true
                    );

                    out.println("What is your name?");

                    String name = in.readLine();

                    out.println(String.format("Hello %s, you've connect through port %d", name, port));
                } catch (IOException e) {
                    System.err.println("Client side error: " + e.getMessage());
                }
            }
        } catch (IOException e) {
            System.err.println("Server side error: " + e.getMessage());
        }
    }
}
