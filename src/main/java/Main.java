import domain.Menu;

public class Main {

    public static void main(String[] args) {
        Menu menu = new Menu();

        menu.start();

        menu.pack();
        menu.setLocationRelativeTo(null);
        menu.setVisible(true);
    }
}