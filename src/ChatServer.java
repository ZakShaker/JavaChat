import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashSet;
import java.util.concurrent.ConcurrentHashMap;

public class ChatServer {

    public static int CODE_LENGTH = 3;
    public static String MENU_CODE = "010";
    public static String MSG_CODE = "020";
    public static String START_CHAT_CODE = "030";

    private static final int PORT = 9099;
    private static ConcurrentHashMap<String, String> users = new ConcurrentHashMap<>();
    private static HashSet<PrintWriter> writers = new HashSet<PrintWriter>();

    public static void main(String[] args) throws Exception {
        System.out.println("The chat server is running.");
        ServerSocket listener = new ServerSocket(PORT);
        try {
            while (true) {
                new Handler(listener.accept()).start();
            }
        } finally {
            listener.close();
        }
    }

    private static class Handler extends Thread {
        private Socket socket;
        private BufferedReader in;
        private PrintWriter out;

        public Handler(Socket socket) {
            this.socket = socket;
        }

        public void run() {
            try {
                String userName = null;

                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                out = new PrintWriter(socket.getOutputStream(), true);

                MenuState menuState = MenuState.START;

                String command = null;
                while (true) {
                    switch (menuState) {
                        case START:
                            out.println(MENU_CODE + "Type: \"1\" to log in or \"2\" to register");
                            command = in.readLine();
                            if ("1".equals(command)) {
                                menuState = MenuState.LOGIN;
                            } else if ("2".equals(command)) {
                                menuState = MenuState.REGISTER;
                            } else {
                                out.println("Please,");
                            }
                            break;
                        case LOGIN:
                            out.println(MENU_CODE + "Please enter your username or \"q\" to return");
                            command = in.readLine();
                            if ("q".equals(command)) {
                                menuState = MenuState.START;
                            } else if (users.containsKey(command)) {
                                userName = command;
                                menuState = MenuState.TYPE_PASSWORD;
                            } else {
                                out.println("No such user. Check username again and ");
                            }
                            break;
                        case TYPE_PASSWORD:
                            out.println(MENU_CODE + "Please enter the password or \"q\" to return");
                            command = in.readLine();
                            if ("q".equals(command)) {
                                menuState = MenuState.LOGIN;
                            } else if (users.get(userName).equals(command)) {
                                menuState = MenuState.CHAT;
                            } else {
                                out.println("The password is not valid,");
                            }
                            break;
                        case REGISTER:
                            out.println(MENU_CODE + "Please create a unique username or \"q\" to return");
                            command = in.readLine();
                            if ("q".equals(command)) {
                                menuState = MenuState.START;
                            } else if (!users.containsKey(command)) {
                                userName = command;
                                menuState = MenuState.CREATE_PASSWORD;
                            } else {
                                out.println("This username already exists,");
                            }
                            break;
                        case CREATE_PASSWORD:
                            out.println(MENU_CODE + "Please create a password \"q\" to return");
                            command = in.readLine();
                            if ("q".equals(command)) {
                                menuState = MenuState.REGISTER;
                            } else {
                                users.put(userName, command);
                                menuState = MenuState.CHAT;
                            }
                            break;
                        case CHAT:
                            writers.add(out);

                            out.println(MENU_CODE + "Welcome to the chat, " + userName + "! To quit the chat enter \"q\"");
                            boolean chatIsOn = true;
                            while (chatIsOn) {
                                String input = in.readLine();
                                if (input == null) {
                                    chatIsOn = false;
                                } else if ("q".equals(input)) {
                                    chatIsOn = false;
                                    writers.remove(out);
                                    menuState = MenuState.START;
                                } else {
                                    for (PrintWriter writer : writers) {
                                        writer.println(MSG_CODE + userName + ": " + input);
                                    }
                                }
                            }
                            break;
                    }
                }
            } catch (IOException e) {
                System.out.println(e);
            } finally {
                if (out != null && writers.contains(out)) {
                    writers.remove(out);
                }

                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}