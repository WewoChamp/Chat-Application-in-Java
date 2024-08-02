import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

public class HomePage {
    public static boolean wasCreation = false;
    public HomePage() throws IOException {
//        Socket socket = new Socket("localhost", 1111);
        Socket socket2 = new Socket("localhost", 2222);
        Socket socket3 = new Socket("localhost", 3333);
        String choice;
        Scanner input = new Scanner(System.in);

        System.out.println(
                "**************************************************");
        System.out.println("Chat Application");
        System.out.println(
                "**************************************************");
        System.out.println("Would you like to: ");
        System.out.println("1. Login to existing account");
        System.out.println("2. Create new account");
        System.out.println("Enter your choice: ");
        choice = input.nextLine();
        choice = choice.trim();

        while(!choice.equalsIgnoreCase("1") && !choice.equalsIgnoreCase("2")) {
            System.out.println("PLEASE ENTER VALID CHOICE");
            System.out.println("Enter your choice: ");
            choice = input.nextLine();
            choice = choice.trim();
        }

        if(choice.equalsIgnoreCase("1")) {
            new LogIn(socket3);
            HomePage.wasCreation = false;
        }

        else if(choice.equalsIgnoreCase("2")) {
            new AccountCreation(socket2, socket3);
            HomePage.wasCreation = true;
        }
    }
}
