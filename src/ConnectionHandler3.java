import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

public class ConnectionHandler3 implements Runnable{
    private Socket socket;
    public static ArrayList<ConnectionHandler3> connections = new ArrayList<>();
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;
    public String userName;
    public String password;

    public ConnectionHandler3(Socket socket) {
        try {
            this.socket = socket;
            this.bufferedReader =
                    new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.bufferedWriter =
                    new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            this.userName = bufferedReader.readLine();
            this.password = bufferedReader.readLine();
            connections.add(this);
        }catch (IOException e) {
            shutdownConnectionHandler(socket, bufferedReader, bufferedWriter);
        }
    }

    @Override
    public void run() {
        String userName = "";
        String password;
        boolean exists = false;
        boolean passwordMatch = false;

        try {
            while (!exists) {
                userName = bufferedReader.readLine();
                exists = doesUserExist(userName);
            }

            while (!passwordMatch) {
                password = bufferedReader.readLine();
                passwordMatch = doesPasswordMatch(userName, password);
            }
        } catch (Exception e) {

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

    public boolean doesUserExist(String username){
        boolean userExists = false;
        for(ConnectionHandler3 connection : connections) {
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

    public boolean doesPasswordMatch(String username, String password){
        boolean passwordsMatch = false;
        for(ConnectionHandler3 connection : connections) {
            if(connection.userName.equals(username)) {
                if(connection.password.equals(password)){
                    passwordsMatch = true;
                }
                break;
            }
        }
        try{
            bufferedWriter.write(String.valueOf(passwordsMatch));
            bufferedWriter.newLine();
            bufferedWriter.flush();
        }catch (IOException e) {
            e.printStackTrace();
        }
        return passwordsMatch;
    }
}

