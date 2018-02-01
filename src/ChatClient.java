import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class ChatClient {

    private int port = 9099;
    private String serverAddress = "127.0.0.1";
    private Scanner scannerLogin;
    private BufferedReader fromServer;
    private PrintWriter toServer;

    public ChatClient() {
        scannerLogin = new Scanner(System.in);
    }

    private void run() {
        boolean chatWorks = true;
        while (chatWorks) {
            try {
                Socket socket = new Socket(serverAddress, port); //throws IOException

                fromServer = new BufferedReader(new InputStreamReader(
                        socket.getInputStream()));
                toServer = new PrintWriter(socket.getOutputStream(), true);

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        boolean userWantsToChat = true;
                        Scanner scanner = new Scanner(System.in);
                        while (userWantsToChat) {
                            String lineFromChat = scanner.nextLine();
                            if ("q".equals(lineFromChat)) {
                                userWantsToChat = false;
                            }
                            toServer.println(lineFromChat);
                        }
                    }
                }).start();


                while (true) {
                    String line = fromServer.readLine();
                    if (line.startsWith(ChatServer.MENU_CODE)) {
                        System.out.println(line.replace(ChatServer.MENU_CODE, ""));
                    } else if (line.startsWith(ChatServer.START_CHAT_CODE)) {
                        System.out.println(line.replace(ChatServer.START_CHAT_CODE, ""));
                    } else if (line.startsWith(ChatServer.MSG_CODE)) {
                        System.out.println(line.replace(ChatServer.MSG_CODE, ""));
                    }
                }
            } catch (IOException e) {
                System.out.println("Cannot connect to the server.\nWould you like to try again or exit?\nEnter '1' to proceed, or 'q' to exit");
                Scanner answerScanner = new Scanner(System.in);
                boolean userAnswered = false;
                while (!userAnswered) {
                    String userAnswer = answerScanner.next();
                    if ("q".equals(userAnswer)) {
                        System.out.println("See you later! Bye-bye :-)");
                        chatWorks = false;
                        userAnswered = true;
                    } else if ("1".equals(userAnswer)) {
                        chatWorks = true;
                        userAnswered = true;
                    }
                }
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