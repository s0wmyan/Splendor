import java.util.*;
public class Test {
    public static void getInput() {
        Scanner sc = new Scanner(System.in);
        String input = sc.nextLine();
        System.out.print("\033[H\033[2J");
        System.out.println(input);
    }
    public static void main(String[] args) {
        while (true) {
            getInput();
        }
    }

}