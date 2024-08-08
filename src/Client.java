import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    private Socket socket;
    private Socket socket2;
    private BufferedReader bufferedReader;
    private BufferedReader bufferedReader2;
    private BufferedWriter bufferedWriter;
    private BufferedWriter bufferedWriter2;
    private String userName;

    public Client(Socket socket, Socket socket2, String userName) {
        try {
            this.socket = socket;
            this.socket2 = socket2;
            this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.bufferedReader2 = new BufferedReader(new InputStreamReader(socket2.getInputStream()));
            this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            this.bufferedWriter2 = new BufferedWriter(new OutputStreamWriter(socket2.getOutputStream()));
            this.userName = userName;
            bufferedWriter.write(userName);
            bufferedWriter.newLine();
            bufferedWriter.flush();
            bufferedWriter2.write(userName);
            bufferedWriter2.newLine();
            bufferedWriter2.flush();
            bufferedWriter2.write("bucsd");
            bufferedWriter2.newLine();
            bufferedWriter2.flush();
            bufferedReader2.readLine();
        } catch (IOException e) {
            shutdownClient(socket, bufferedReader, bufferedWriter);
        }
    }

    public void sendMessage() {
        try {
            Scanner scanner = new Scanner(System.in);
            while (socket.isConnected()) {
                String messageToSend = scanner.nextLine();
                bufferedWriter.write(userName + ": " + messageToSend);
                bufferedWriter.newLine();
                bufferedWriter.flush();
                if (messageToSend.contains("/quit")) {
                    shutdownClient(socket, bufferedReader, bufferedWriter);
                    bufferedWriter2.write(String.valueOf(false));
                    bufferedWriter2.newLine();
                    bufferedWriter2.flush();
                    new HomePage();
                }
            }
        } catch (IOException e) {
            shutdownClient(socket, bufferedReader, bufferedWriter);
        }
    }

    public void listenForMessages() {
        new Thread(new Runnable() {
            public void run() {
                String messageFromChat;
                while (socket.isConnected()) {
                    try {
                        messageFromChat = bufferedReader.readLine();
                        if (messageFromChat != null) {
                            System.out.println(messageFromChat);
                        }
                    } catch (IOException e) {
                        shutdownClient(socket, bufferedReader, bufferedWriter);
                    }
                }
            }
        }).start();
    }

    public void shutdownClient(Socket socket, BufferedReader bufferedReader, BufferedWriter bufferedWriter) {
        try {
            if (bufferedReader != null) {
                bufferedReader.close();
            }
            if (bufferedWriter != null) {
                bufferedWriter.close();
            }
            if (socket != null) {
                socket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}

//    public static void main(String[] args) throws IOException {
//        Scanner scanner = new Scanner(System.in);
//        System.out.println("Please enter your username: ");
//        String userName = scanner.nextLine();
//        Socket socket = new Socket("localhost", 1111);
//        Client client = new Client(socket, userName);
//        client.listenForMessages();
//        client.sendMessage();
//    }
