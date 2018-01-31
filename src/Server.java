import com.oracle.javafx.jmx.json.JSONReader;
import com.oracle.javafx.jmx.json.JSONWriter;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket =
                new ServerSocket(9099);

        Socket socket = serverSocket.accept();

        DataInputStream dataInputStream =
                new DataInputStream(socket.getInputStream());

        DataOutputStream dataOutputStream =
                new DataOutputStream(socket.getOutputStream());


        String mes;
        while (true) {
            mes = dataInputStream.readUTF();
            if ("q".equals(mes))
                return;
            else {
                System.out.println("Клиент написал: " + mes);
                dataOutputStream.writeUTF(mes);
            }
        }
    }
}
