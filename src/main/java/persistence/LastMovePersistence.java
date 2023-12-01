package persistence;

import database.MyDataBase;

public class LastMovePersistence {

    private final MyDataBase db = MyDataBase.getInstance();

    public void createLastMove() {
        String sql = """
                insert into game.last_move
                (color)
                values
                ('java.awt.Color[r=0,g=0,b=0]')
                """;
        db.execute(String.format(sql));
    }

    public void updateLastMove(String color) {
        String sql = """
                update game.last_move
                set color = '%s'
                """;
        db.execute(String.format(sql, color));
    }
}