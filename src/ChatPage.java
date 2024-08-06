import java.io.IOException;
import java.net.Socket;

public class ChatPage {
    public ChatPage() {
        System.out.println(
                "**************************************************");
        if(AccountCreation.userName != null){
            System.out.println(AccountCreation.userName + "'s Chat Page");
        }else{
            System.out.println(LogIn.userName + "'s Chat Page");
        }
        System.out.println(
                "**************************************************");
        System.out.println("Type texts and press enter to send");
        System.out.println(
                "**************************************************");
        System.out.println("Enter \"quit\" to leave the chat");
        System.out.println(
                "**************************************************");
        if(AccountCreation.userName != null) {
            try {
                Socket socket = new Socket("localhost", 1111);
                Client client = new Client(socket, AccountCreation.userName);
                client.listenForMessages();
                client.sendMessage();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else{
            try {
                Socket socket = new Socket("localhost", 1111);
                Client client = new Client(socket, LogIn.userName);
                client.listenForMessages();
                client.sendMessage();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
