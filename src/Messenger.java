public interface Messenger {
    void receiveMessages(Client client, String msg);
    void postMessage(Client client, String msg);
}
