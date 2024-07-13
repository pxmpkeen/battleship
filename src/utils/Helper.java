package utils;

public class Helper {
    public static void numClear(int count) {
        for (int i = 0; i < count; i++) {
            System.out.print("\033[F"); // Move cursor up one line
            System.out.print("\r\033[K"); // Move cursor to beginning of the line and clear the line
        }
    }
}
