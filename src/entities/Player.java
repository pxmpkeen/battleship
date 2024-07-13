package entities;

import services.Sea;

import java.util.Scanner;

public class Player {
    String name;
    static int playerCount = 1;
    Sea ownBoard, enemyBoard;

    public Player() {
        System.out.printf("Name of player %d: ", playerCount++);
        name = new Scanner(System.in).nextLine();
        ownBoard = new Sea();
        enemyBoard = new Sea();
    }

    public Sea getOwnBoard() {
        return ownBoard;
    }

    public String getName() {
        return name;
    }

    public Sea getEnemyBoard() {
        return enemyBoard;
    }
}
