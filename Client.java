import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Client {
    static void main() {
        String ip = "localhost";
        int port = 12005;

        try (Socket socket = new Socket(ip, port)) {
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(socket.getInputStream())
            );

            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

            String greeting = in.readLine();
            System.out.println("Server: " + greeting);

            String name = "Anton";
            out.println(name);

            String answer = in.readLine();
            System.out.println("Server: " + answer);
        } catch(IOException e) {
            System.err.println("Connection problem: " + e.getMessage());
        }
    }
}
