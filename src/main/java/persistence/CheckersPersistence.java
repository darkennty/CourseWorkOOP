package persistence;

import database.MyDataBase;

public class CheckersPersistence {

    private final MyDataBase db = MyDataBase.getInstance();

    public void createChecker(int id, int xAxis, int yAxis, String borderColor, String color) {
        String sql = """
                insert into game.field
                (id, xAxis, yAxis, borderColor, color)
                values
                (%d, %d, %d, '%s', '%s')
                """;
        db.execute(String.format(sql, id, xAxis, yAxis, borderColor, color));
    }

    public void updateChecker(int id, int xAxis, int yAxis, String borderColor, String color) {
        String sql = """
                update game.field
                set xAxis = %d,
                    yAxis = %d,
                    borderColor = '%s',
                    color = '%s'
                where id = %d
                """;
        db.execute(String.format(sql, xAxis, yAxis, borderColor, color, id));
    }
}