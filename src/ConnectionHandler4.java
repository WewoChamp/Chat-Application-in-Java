import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

public class ConnectionHandler4 implements Runnable{
    private Socket socket;
    public static ArrayList<ConnectionHandler4> connections =
            new ArrayList<>();
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;
    public String userName;

    public ConnectionHandler4(Socket socket) {
        try {
            this.socket = socket;
            this.bufferedReader =
                    new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.bufferedWriter =
                    new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            this.userName = bufferedReader.readLine();
            connections.add(this);
        }catch (IOException e) {
            shutdownConnectionHandler(socket, bufferedReader, bufferedWriter);
        }
    }

    @Override
    public void run() {
        String userName = "";
        boolean loggedIn = true;
        while (loggedIn) {
            try {
                userName = bufferedReader.readLine();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            loggedIn = isLoggedIn(userName);
        }
        try {
            loggedIn = Boolean.parseBoolean(bufferedReader.readLine());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        if (!loggedIn) {
            shutdownConnectionHandler(socket, bufferedReader, bufferedWriter);
        }
    }

    public void removeConnection(){
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

    public boolean isLoggedIn(String userName){
        boolean loggedIn = false;
        for (ConnectionHandler4 connection : connections) {
            if(connection.userName.equals(userName)) {
                loggedIn = true;
                break;
            }
        }
        try{
            bufferedWriter.write(String.valueOf(loggedIn));
            bufferedWriter.newLine();
            bufferedWriter.flush();
        }catch(IOException e){
            e.printStackTrace();
        }
        return loggedIn;
    }
}


