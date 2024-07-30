import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    private Socket socket;
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;
    private String userName;

    public Client(Socket socket, String userName) {
        try {
            this.socket = socket;
            this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            this.userName = userName;
        }catch (IOException e){
            shutdownClient(socket, bufferedReader, bufferedWriter);
        }
    }

    public void sendMessage() {
        try{
            bufferedWriter.write(userName);
            bufferedWriter.newLine();
            bufferedWriter.flush();

            Scanner scanner = new Scanner(System.in);
            while(socket.isConnected()){
                String messageToSend = scanner.nextLine();
                bufferedWriter.write(userName + ": " + messageToSend);
                bufferedWriter.newLine();
                bufferedWriter.flush();
            }
        }catch(IOException e){
            shutdownClient(socket, bufferedReader, bufferedWriter);
        }
    }

    public void listenForMessages() {
        new Thread(new Runnable() {
            public void run() {
                String messageFromChat;

                while(socket.isConnected()){
                    try {
                        messageFromChat = bufferedReader.readLine();
                        System.out.println(messageFromChat);
                    }catch(IOException e){
                        shutdownClient(socket, bufferedReader, bufferedWriter);
                    }
                }
            }
        }).start();
    }

    public void shutdownClient(Socket socket, BufferedReader bufferedReader, BufferedWriter bufferedWriter) {
        try{
            if(bufferedReader != null) {
                bufferedReader.close();
            }
            if(bufferedWriter != null) {
                bufferedWriter.close();
            }
            if(socket != null) {
                socket.close();
            }
        }catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws IOException {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Please enter your username: ");
        String userName = scanner.nextLine();
        Socket socket = new Socket("localhost", 1111);
        Client client = new Client(socket, userName);
        client.listenForMessages();
        client.sendMessage();
    }
}
