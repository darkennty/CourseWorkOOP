package persistence;

import database.MyDataBase;

public class CheckersPersistence {

    private final MyDataBase db = MyDataBase.getInstance();

    public void createChecker(int id, int xAxis, int yAxis, String color) {
        String sql = """
                insert into game.field
                (id, xAxis, yAxis, color)
                values
                (%d, %d, %d, '%s')
                """;
        db.execute(String.format(sql, id, xAxis, yAxis, color));
    }

    public void updateChecker(int id, int xAxis, int yAxis, String color) {
        String sql = """
                update game.field
                set xAxis = %d,
                    yAxis = %d,
                    color = '%s'
                where id = %d
                """;
        db.execute(String.format(sql, xAxis, yAxis, color, id));
    }
}