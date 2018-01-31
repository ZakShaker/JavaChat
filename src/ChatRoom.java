import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class ChatRoom implements Messenger {
    private ArrayList<Client> clients = new ArrayList<>();
    private String roomName;
    private int port;
    ServerSocket serverSocket;
    DataInputStream inputStream;
    DataOutputStream outputStream;

    public ChatRoom(String roomName, int port) throws IOException {
        this.roomName = roomName;
        this.port = port;
        serverSocket = new ServerSocket(port);
        Socket socket = serverSocket.accept();
        inputStream = new DataInputStream(socket.getInputStream());
        outputStream = new DataOutputStream(socket.getOutputStream());
    }

    public String getRoomName() {
        return roomName;
    }

    public int getPort() {
        return port;
    }

    @Override
    public void receiveMessages(Client client, String msg) {

    }

    @Override
    public void postMessage(Client client, String msg) {

    }

    public static void main(String args[]) {
    }
}