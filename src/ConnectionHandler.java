import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

public class ConnectionHandler implements Runnable{
    private Socket socket;
    private static ArrayList<ConnectionHandler> connections = new ArrayList<>();
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
            connections.add(this);
            broadcastMessage(userName + " joined the chat!");
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
                shutdownConnectionHandler(socket, bufferedReader, bufferedWriter);
            }
        }
    }

    public void removeConnection(){
        connections.remove(this);
        broadcastMessage(userName + " left the chat!");
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
