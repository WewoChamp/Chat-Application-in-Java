import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class LogIn {
    public static String userName;
    public static boolean wasCreated = false;
    private Socket socket;
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;

    public LogIn(Socket socket) {
        LogIn.wasCreated = true;
        try{
            this.socket = socket;
            this.bufferedReader =
                    new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.bufferedWriter =
                    new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        }catch(Exception e){
            e.printStackTrace();
        }
        String userName = "";
        String password;
        Scanner input = new Scanner(System.in);

        //try block below in place to satisfy the buffer stream
        try{
            bufferedWriter.write("dssdcaucfudscydcv");
            bufferedWriter.newLine();
            bufferedWriter.flush();
            bufferedWriter.write("cahscgdycdydyu");
            bufferedWriter.newLine();
            bufferedWriter.flush();
        }catch(Exception e){
            e.printStackTrace();
        }

        System.out.println(
                "**************************************************");
        System.out.println("Log In");
        System.out.println(
                "**************************************************");
        System.out.println("Please enter your username or 0 to go back: ");
        userName = input.nextLine();
        userName = userName.trim();

        try {
            bufferedWriter.write(userName);
            bufferedWriter.newLine();
            bufferedWriter.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }

        while (!doesUserExist(userName) && !userName.equals("0")) {
            System.out.println("USERNAME DOESN'T EXIST!");
            System.out.println("Please enter your username or 0 to go back: ");
            userName = input.nextLine();
            userName = userName.trim();

            try {
                bufferedWriter.write(userName);
                bufferedWriter.newLine();
                bufferedWriter.flush();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if (userName.equals("0")) {
            try {
                new HomePage();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        LogIn.userName = userName;

        System.out.println("Please enter your password or 0 to go back: ");
        password = input.nextLine();
        password = password.trim();

        try{
            bufferedWriter.write(password);
            bufferedWriter.newLine();
            bufferedWriter.flush();
        }catch(Exception e){
            e.printStackTrace();
        }

        while(!doesPasswordMatch(userName, password) && !password.equals("0")){
            System.out.println("INCORRECT PASSWORD!");
            System.out.println("Please enter your password or 0 to go back: ");
            password = input.nextLine();
            password = password.trim();

            try{
                bufferedWriter.write(password);
                bufferedWriter.newLine();
                bufferedWriter.flush();
            }catch(Exception e){
                e.printStackTrace();
            }
        }

        if(password.equals("0")){
            try {
                new HomePage();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        System.out.println("Welcome back!");
        new ChatPage();
    }

    public boolean doesUserExist(String userName){
        boolean userExists = false;
        try {
            userExists = Boolean.parseBoolean(bufferedReader.readLine());
        }catch (IOException e){
            e.printStackTrace();
        }
        return userExists;
    }

    public boolean doesPasswordMatch(String userName, String password){
        boolean passwordMatch = false;
        try{
            passwordMatch = Boolean.parseBoolean(bufferedReader.readLine());
        } catch (IOException e){

        }
        return passwordMatch;
    }
}
