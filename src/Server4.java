import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server4 {
    private ServerSocket serverSocket;
    private boolean serverRunning;

    public Server4(ServerSocket serverSocket) {
        this.serverSocket = serverSocket;
        serverRunning = true;
    }

    public void startServer(){
        try{
            while(serverRunning){
                Socket socket = serverSocket.accept();
                ConnectionHandler4 connection = new ConnectionHandler4(socket);
                Thread thread = new Thread(connection);
                thread.start();
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
        ServerSocket serverSocket = new ServerSocket(4444);
        Server4 server = new Server4(serverSocket);
        server.startServer();
    }
}


