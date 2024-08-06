import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class AccountCreation {
    public static String userName;
    private Socket socket;
    private Socket socket2;
    private BufferedReader bufferedReader;
    private BufferedReader bufferedReader2;
    private BufferedWriter bufferedWriter;
    private BufferedWriter bufferedWriter2;

    public AccountCreation(Socket socket, Socket socket2) {
        try{
            this.socket = socket;
            this.socket2 = socket2;
            this.bufferedReader =
                    new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.bufferedReader2 = new BufferedReader(new InputStreamReader(socket2.getInputStream()));
            this.bufferedWriter =
                    new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            this.bufferedWriter2 = new BufferedWriter(new OutputStreamWriter(socket2.getOutputStream()));
        }catch(Exception e){
            e.printStackTrace();
        }

        String userName;
        String password;
        Scanner input = new Scanner(System.in);

        System.out.println(
                "**************************************************");
        System.out.println("Account Creation");
        System.out.println(
                "**************************************************");
        System.out.println("Please create your username or enter 0 to go " +
                "back: ");
        userName = input.nextLine();
        userName = userName.trim();

        try{
            bufferedWriter.write(userName);
            bufferedWriter.newLine();
            bufferedWriter.flush();
        }catch(Exception e){
            e.printStackTrace();
        }
        while(doesUserExist() && !userName.equals("0")){
            System.out.println("USERNAME ALREADY EXISTS!");
            System.out.println("Please choose another username or enter 0 to " +
                    "go back: ");
            userName = input.nextLine();
            userName = userName.trim();
            try{
                bufferedWriter.write(userName);
                bufferedWriter.newLine();
                bufferedWriter.flush();
            }catch(Exception e){

            }
        }

        if(userName.equals("0")){
            try {
                new HomePage();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        this.userName = userName;

        System.out.println("Please create your password or enter 0 to go " +
                "back: ");
        password = input.nextLine();
        password = password.trim();

        while(password.isEmpty()){
            System.out.println("YOU MUST CHOOSE A PASSWORD!");
            System.out.println("Please create your password or enter 0 to go " +
                    "back: ");
            password = input.nextLine();
            password = password.trim();
        }

        if(password.equals("0")){
            try {
                new HomePage();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        try {
            bufferedWriter.write(password);
            bufferedWriter.newLine();
            bufferedWriter.flush();
        }catch (IOException e){

        }

        try{
            bufferedWriter2.write(userName);
            bufferedWriter2.newLine();
            bufferedWriter2.flush();
            bufferedWriter2.write(password);
            bufferedWriter2.newLine();
            bufferedWriter2.flush();
            bufferedWriter2.write(userName);
            bufferedWriter2.newLine();
            bufferedWriter2.flush();
            bufferedReader2.readLine();
            bufferedWriter2.write(password);
            bufferedWriter2.newLine();
            bufferedWriter2.flush();
            bufferedReader2.readLine();

        }catch (IOException e){

        }

        System.out.println("Your account has been created!");
        new ChatPage();
    }

    public boolean doesUserExist(){
        boolean userExists = false;
        try {
            userExists = Boolean.parseBoolean(bufferedReader.readLine());
        }catch (IOException e){
            e.printStackTrace();
        }
        return userExists;
    }
}
