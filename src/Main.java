import entities.Player;
import services.Sea;
import utils.Helper;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Player player1 = new Player();
        player1.getOwnBoard().fillBoard();

        Player player2 = new Player();
        player2.getOwnBoard().fillBoard();


        boolean playing = true;
        boolean queue = true;
        while (playing) {
            try {
                Sea current, predictions;
                if (queue) {
                    current = player2.getOwnBoard();
                    predictions = player2.getEnemyBoard();
                } else {
                    current = player1.getOwnBoard();
                    predictions = player1.getEnemyBoard();
                }
                System.out.println("Turn of player " + (queue ? "1" : "2"));
                System.out.println("Give position (e.g. B4)");
                String position = new Scanner(System.in).nextLine();
                Helper.numClear(27);
                int y = position.toUpperCase().charAt(0) - 'A';
                int x = position.charAt(1) - '0';

                predictions.check(x, y);
                if (!current.hit(x, y)) {
                    queue = !queue;
                } else {
                    predictions.fillOne(x, y);
                }
                System.out.println(queue ? player1.getOwnBoard() : player2.getOwnBoard());
                System.out.println(queue ? player2.getEnemyBoard() : player1.getEnemyBoard());

                if (current.finish()) {
                    playing = false;
                    System.out.println("Winner is " + (queue ? "Player 1 " + player1.getName() : "Player 2 " + player2.getName()));
                }
            } catch (Exception ignored) {

            }
        }
    }
}