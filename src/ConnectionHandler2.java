import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

public class ConnectionHandler2 implements Runnable{
    private Socket socket;
    public static ArrayList<ConnectionHandler2> connections =
            new ArrayList<>();
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;
    public String userName;
    public String password;

    public ConnectionHandler2(Socket socket) {
        try {
            this.socket = socket;
            this.bufferedReader =
                    new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.bufferedWriter =
                    new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        }catch (IOException e) {
            shutdownConnectionHandler(socket, bufferedReader, bufferedWriter);
        }
    }

    @Override
    public void run() {
        String userName = "";
        String password;
        boolean exists = true;

        try {
            while (exists) {
                userName = bufferedReader.readLine();
                exists = doesUserExist(userName);
            }

            password = bufferedReader.readLine();

            addUser(userName, password);
            connections.add(this);

        } catch (Exception e) {

        }
    }

    public void broadcastMessage(String messageToSend) {
        for (ConnectionHandler2 connection : connections) {
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

    public boolean doesUserExist(String username){
        boolean userExists = false;
        for(ConnectionHandler2 connection : connections) {
            if(connection.userName.equals(username)) {
                userExists = true;
                break;
            }
        }
        try{
            bufferedWriter.write(String.valueOf(userExists));
            bufferedWriter.newLine();
            bufferedWriter.flush();
        }catch (IOException e) {
            e.printStackTrace();
        }
        return userExists;
    }

    public void addUser(String username, String password){
        this.userName = username;
        this.password = password;
    }
}

