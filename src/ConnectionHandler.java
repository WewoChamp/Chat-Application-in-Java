import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

public class ConnectionHandler implements Runnable{
    private Socket socket;
    public static ArrayList<ConnectionHandler> connections = new ArrayList<>();
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;
    public String userName;

    public ConnectionHandler(Socket socket) {
        try {
            this.socket = socket;
            this.bufferedReader =
                    new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.bufferedWriter =
                    new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            this.userName = bufferedReader.readLine();
            broadcastMessage(this.userName + " has joined the chat.");
            connections.add(this);
        }catch (IOException e) {
            shutdownConnectionHandler(socket, bufferedReader, bufferedWriter);
        }
    }

    @Override
    public void run() {
        String messageFromClient;

        while(socket.isConnected()) {
            try{
                messageFromClient = bufferedReader.readLine();
                if(messageFromClient.contains("/quit")) {
                    shutdownConnectionHandler(socket, bufferedReader, bufferedWriter);
                    break;
                }
                broadcastMessage(messageFromClient);
            }catch(IOException e){
                shutdownConnectionHandler(socket, bufferedReader, bufferedWriter);
            }
        }

    }

    public void broadcastMessage(String messageToSend) {
        for (ConnectionHandler connection : connections) {
            try{
                if(!connection.userName.equals(userName)) {
                    connection.bufferedWriter.write(messageToSend);
                    connection.bufferedWriter.newLine();
                    connection.bufferedWriter.flush();
                }
            }catch(IOException e){
                e.printStackTrace();
            }
        }
    }

    public void removeConnection(){
        broadcastMessage(this.userName + " left the chat!");
        connections.remove(this);
    }

    public void shutdownConnectionHandler(Socket socket, BufferedReader bufferedReader, BufferedWriter bufferedWriter) {
        removeConnection();
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
}
