import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class ChatClient {

    Thread screen = new Thread(new Runnable() {
        @Override
        public void run() {
            while (true)
                out.println(getLineFromChat());
        }
    });

    private int port = 9099;
    private String serverAddress = "127.0.0.1";
    private Scanner scannerLogin;
    private BufferedReader in;
    private PrintWriter out;
    public ChatClient() {
        scannerLogin = new Scanner(System.in);
    }

    private String getLineFromChat() {
        while (true) {
            String lineFromChat = scannerLogin.next();
            if (!"q".equals(lineFromChat)) {
                return lineFromChat;
            }
            if ("q".equals(lineFromChat)) {
                return null;
            }
        }
    }

    private void run() throws IOException {

        Socket socket = new Socket(serverAddress, port);
        in = new BufferedReader(new InputStreamReader(
                socket.getInputStream()));
        out = new PrintWriter(socket.getOutputStream(), true);

        while (true) {
            String line = in.readLine();
            if (line.startsWith("Choose login")) {
                System.out.println(line);
                out.println(getLineFromChat());
            } else if (line.startsWith("Name is free")) {
                System.out.println("Welcome to the chat! Start typing");
                screen.start();
            } else if (line.startsWith("msg")) {
                System.out.print("\033[H\033[2J");
                System.out.flush();
                System.err.println(line.substring(8) + "\n");
            }
        }
    }

    /**
     * Runs the client as an application with a closeable frame.
     */
    public static void main(String[] args) throws Exception {
        ChatClient client = new ChatClient();
        client.run();
    }
}