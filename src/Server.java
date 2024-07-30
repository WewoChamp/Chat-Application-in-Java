import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    private ServerSocket serverSocket;
    private boolean serverRunning = true;

    public Server(ServerSocket serverSocket) {
        this.serverSocket = serverSocket;
    }

    public void startServer(){
        try{
            while(serverRunning){
                Socket socket = serverSocket.accept();
                ConnectionHandler connection = new ConnectionHandler(socket);
                System.out.println(connection.userName + " connected");

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
        ServerSocket serverSocket = new ServerSocket(1111);
        Server server = new Server(serverSocket);
        server.startServer();
    }
}
