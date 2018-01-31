import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    private String login;
    private String password;

    public static void main(String[] args) throws IOException {
        Socket socket = new Socket("127.0.0.1", 9099);

        DataOutputStream dataOutputStream =
                new DataOutputStream(socket.getOutputStream());

        DataInputStream dataInputStream =
                new DataInputStream(socket.getInputStream());


        Scanner scanner = new Scanner(System.in);
        String mes = "";
        String msgFromServer;

        while (true) {
            if (!"q".equals(mes)) {
                mes = scanner.nextLine();
                dataOutputStream.writeUTF(mes);
                dataOutputStream.flush();
            } else {
                dataOutputStream.close();
                break;
            }

            msgFromServer = dataInputStream.readUTF();
            if (!msgFromServer.equals(mes)) {
                System.out.println("Кто-то написал: " + msgFromServer);
            }
        }
    }
}
