package database;

import config.DatabaseProperties;
import config.PropertiesFactory;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;

public class MyDataBase {
    private static MyDataBase instance;

    private final DatabaseProperties properties = PropertiesFactory.getProperties();

    private MyDataBase() {
        init();
    }

    public synchronized static MyDataBase getInstance() {
        if (instance == null) {
            instance = new MyDataBase();
        }

        return instance;
    }


    private void init() {
        createSchema();
        createTableChecker();
        createTableLastMove();
    }

    public void createSchema() {
        String sql = """
                create schema if not exists game;
                """;
        execute(sql);
    }

    private void createTableChecker() {
        String sql = """
        create table if not exists game.field (
            id serial primary key,
            xAxis integer,
            yAxis integer,
            color text
        )
        """;

        execute(sql);
    }

    private void createTableLastMove() {
        String sql = """
        create table if not exists game.last_move (
            color text primary key
        )
        """;

        execute(sql);
    }

    public Map<Integer, int[]> getWhiteCheckers() {
        Map<Integer, int[]> data = new HashMap<>();
        String sql = "SELECT id, xAxis, yAxis FROM game.field where color = 'java.awt.Color[r=255,g=255,b=255]'";

        try (Connection connection = connect();
             Statement statement = connection.createStatement()) {
            ResultSet set = statement.executeQuery(sql);

            while (set.next()) {
                data.put(set.getInt("id"), new int[] {
                        set.getInt("xAxis"),
                        set.getInt("yAxis")
                });
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }

        return data;
    }

    public Map<Integer, int[]> getBlackCheckers() {
        Map<Integer, int[]> data = new HashMap<>();
        String sql = "SELECT id, xAxis, yAxis FROM game.field where color = 'java.awt.Color[r=0,g=0,b=0]'";

        try (Connection connection = connect();
             Statement statement = connection.createStatement()) {
            ResultSet set = statement.executeQuery(sql);

            while (set.next()) {
                data.put(set.getInt("id"), new int[] {
                        set.getInt("xAxis"),
                        set.getInt("yAxis")
                });
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }

        return data;
    }

    public Map<Integer, int[]> getWhiteQueens() {
        Map<Integer, int[]> data = new HashMap<>();
        String sql = "SELECT id, xAxis, yAxis FROM game.field where color = 'java.awt.Color[r=192,g=192,b=192]'";

        try (Connection connection = connect();
             Statement statement = connection.createStatement()) {
            ResultSet set = statement.executeQuery(sql);

            while (set.next()) {
                data.put(set.getInt("id"), new int[] {
                        set.getInt("xAxis"),
                        set.getInt("yAxis")
                });
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }

        return data;
    }

    public Map<Integer, int[]> getBlackQueens() {
        Map<Integer, int[]> data = new HashMap<>();
        String sql = "SELECT id, xAxis, yAxis FROM game.field where color = 'java.awt.Color[r=64,g=64,b=64]'";

        try (Connection connection = connect();
             Statement statement = connection.createStatement()) {
            ResultSet set = statement.executeQuery(sql);

            while (set.next()) {
                data.put(set.getInt("id"), new int[] {
                        set.getInt("xAxis"),
                        set.getInt("yAxis")
                });
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }

        return data;
    }

    public Map<Integer, int[]> getAllFields() {
        Map<Integer, int[]> data = new HashMap<>();
        String sql = "SELECT id, xAxis, yAxis FROM game.field";

        try (Connection connection = connect();
             Statement statement = connection.createStatement()) {
            ResultSet set = statement.executeQuery(sql);

            while (set.next()) {
                data.put(set.getInt("id"), new int[] {set.getInt("xAxis"), set.getInt("yAxis")});
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }

        return data;
    }

    public void deleteCheckers() {
        String sql = """
                delete from game.field
                """;

        try (Connection connection = connect();
             Statement statement = connection.createStatement()) {
            statement.executeUpdate(sql);
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }

    public void execute(String sql) {
        try (Connection connection = connect();
             Statement statement = connection.createStatement()) {
            statement.execute(sql);
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }

    public String getLastMove() {
        String sql = "SELECT color FROM game.last_move";

        String result = "";

        try (Connection connection = connect();
             Statement statement = connection.createStatement()) {
            ResultSet set = statement.executeQuery(sql);

            if (set.next()) {
                result = set.getString("color");
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }

        return result;
    }

    public void deleteLastMove() {
        String sql = "DELETE FROM game.last_move";

        try (Connection connection = connect();
             Statement statement = connection.createStatement()) {
            statement.executeUpdate(sql);
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }

    public Connection connect() throws SQLException {
        return DriverManager.getConnection(
                properties.getUrl(),
                properties.getLogin(),
                properties.getPassword()
        );
    }
}