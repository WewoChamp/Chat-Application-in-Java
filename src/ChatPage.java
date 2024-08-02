import java.io.IOException;
import java.net.Socket;

public class ChatPage {
    public ChatPage() {
        System.out.println(
                "**************************************************");
        System.out.println("Chat Page: " + AccountCreation.userName);
        System.out.println(
                "**************************************************");
        System.out.println("Type texts and press enter to send");
        System.out.println(
                "**************************************************");
        try {
            Socket socket = new Socket("localhost", 1111);
            Client client = new Client(socket, AccountCreation.userName);
            client.listenForMessages();
            client.sendMessage();
        }catch (IOException e) {
            e.printStackTrace();
        }
    }
}
