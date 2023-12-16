import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import database.MyDataBase;
import persistence.CheckersPersistence;
import persistence.LastMovePersistence;

import java.awt.*;
import java.util.Map;

import static consts.Consts.SIZE;
import static org.assertj.core.api.BDDAssertions.then;

public class MyDataBase_Test {

    private final MyDataBase db = MyDataBase.getInstance();
    private final CheckersPersistence checkersPersistence = new CheckersPersistence();
    private final LastMovePersistence lastMovePersistence = new LastMovePersistence();

    @Test
    @DisplayName("checking getWhiteCheckers() method")
    public void checkWhiteCheckersQuantity() {
        db.deleteCheckers();

        createCheckersInDB();

        Map<Integer, String[]> data = db.getWhiteCheckers();

        then(data.size()).isEqualTo(12);
    }

    @Test
    @DisplayName("checking getWhiteQueens() method")
    public void checkWhiteQueensQuantity() {
        db.deleteCheckers();

        createCheckersInDB();

        Map<Integer, String[]> data = db.getWhiteQueens();

        then(data.size()).isEqualTo(1);
    }

    @Test
    @DisplayName("checking getWhiteCheckers() method")
    public void checkBlackCheckersQuantity() {
        db.deleteCheckers();

        createCheckersInDB();

        Map<Integer, String[]> data = db.getBlackCheckers();

        then(data.size()).isEqualTo(12);
    }

    @Test
    @DisplayName("checking getWhiteQueens() method")
    public void checkBlackQueensQuantity() {
        db.deleteCheckers();

        createCheckersInDB();

        Map<Integer, String[]> data = db.getBlackQueens();

        then(data.size()).isEqualTo(1);
    }

    @Test
    @DisplayName("when there is nothing in database")
    public void whenNothingInDB() {
        db.deleteCheckers();

        Map<Integer, String[]> data = db.getAllFields();
        then(data.size()).isEqualTo(0);
    }

    @Test
    @DisplayName("check getLastMove() method")
    public void checkLastMove() {
        db.deleteLastMove();

        createLastMoveColorInDB();

        String color = db.getLastMove();

        then(color).isEqualTo(Color.WHITE.toString());
    }

    public void createCheckersInDB() {
        int counter = 0;

        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {

                if (i == 4 && j == 4) {
                    checkersPersistence.createChecker(counter, i, j, Color.GREEN.toString(), Color.LIGHT_GRAY.toString());
                } else if (i == 3 && j == 3) {
                    checkersPersistence.createChecker(counter, i, j, Color.GREEN.toString(), Color.DARK_GRAY.toString());
                } else if ((i + j) % 2 == 1) {
                    if (i <= 2) {
                        checkersPersistence.createChecker(counter, i, j, Color.GREEN.toString(), Color.BLACK.toString());
                    } else if (i >= 5) {
                        checkersPersistence.createChecker(counter, i, j, Color.GREEN.toString(), Color.WHITE.toString());
                    } else {
                        checkersPersistence.createChecker(counter, i, j, Color.GREEN.toString(), Color.YELLOW.toString());
                    }
                } else {
                    checkersPersistence.createChecker(counter, i, j, Color.GREEN.toString(), Color.YELLOW.toString());
                }
                counter++;
            }
        }
    }

    public void createLastMoveColorInDB() {
        lastMovePersistence.createLastMove();
        lastMovePersistence.updateLastMove(Color.WHITE.toString());
    }
}
