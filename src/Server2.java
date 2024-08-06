import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server2 {
    private ServerSocket serverSocket;
    private boolean serverRunning;

    public Server2(ServerSocket serverSocket) {
        this.serverSocket = serverSocket;
        serverRunning = true;
    }

    public void startServer(){
        try{
            while(serverRunning){
                Socket socket = serverSocket.accept();
                ConnectionHandler2 connection = new ConnectionHandler2(socket);

                Thread thread = new Thread(connection);
                thread.start();
                //System.out.println(connection.userName + " connected");
            }
        }catch(Exception e){
            shutdownServer();
        }
    }

    public void shutdownServer(){
        try{
            if(serverSocket != null){
                serverSocket.close();
                serverRunning = false;
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(2222);
        Server2 server = new Server2(serverSocket);
        server.startServer();
    }
}

