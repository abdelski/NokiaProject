import model.Models;

import java.sql.SQLException;
import java.util.Scanner;

import static java.lang.System.exit;

public class Main {

    public static void main(String[] args) {

        try {
            Models models = new Models();
            models.printMenu();
            while (true) {
                Scanner sc = new Scanner(System.in);
                String cmd = sc.nextLine();
                if (cmd.equals("program -br")) exit(0);
                if (!cmd.isEmpty()) models.readCmd(cmd);
                System.out.println("new command");

            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }

    }

}