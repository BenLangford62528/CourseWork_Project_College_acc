package services;

import models.Game;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class GameService {

    public static void selectAll(List<Game> targetList, DatabaseConnectionService database) {

        PreparedStatement statement = database.newStatement("SELECT InstanceID, UserID, Date, PlayTime FROM Game ORDER BY InstanceID");

        try {
            if (statement != null) {

                ResultSet results = database.executeQuery(statement);

                if (results != null) {
                    while (results.next()) {
                        targetList.add(new Game(results.getInt("InstanceID"), results.getInt("UserID"), results.getDate("Date"), results.getString("PlayTime")));
                    }
                }
            }
        } catch (SQLException resultsException) {
            System.out.println("Database select all error: " + resultsException.getMessage());
        }
    }

    public static void save(Game game, DatabaseConnectionService database) {
        Game existingItem = null;
        if (game.getInstanceID() != 0) existingItem = selectById(game.getInstanceID(), database);
        try {
            if (existingItem == null) {
                PreparedStatement statement = database.newStatement("INSERT INTO Game (UserID, Date, PlayTime) VALUES (?, ?, ?)");
                statement.setInt(1, game.getUserID());
                statement.setString(2, game.getDateString());
                statement.setString(3, game.getPlayTime());
                database.executeUpdate(statement);
            } else {
                PreparedStatement statement = database.newStatement("UPDATE Game SET UserID = ?, Date = ?, PlayTime = ? WHERE InstanceID = ?");
                statement.setInt(1, game.getUserID());
                statement.setString(2, game.getDateString());
                statement.setString(3, game.getPlayTime());
                statement.setInt(4, game.getInstanceID());
                database.executeUpdate(statement);
            }
        } catch (SQLException resultsException) {
            System.out.println("Database saving error: " + resultsException.getMessage());
        }
    }

    public static Game selectById(int id, DatabaseConnectionService database) {

        Game result = null;

        PreparedStatement statement = database.newStatement("SELECT InstanceID, UserID, Date, PlayTime FROM Game WHERE InstanceID = ?");

        try {
            if (statement != null) {

                statement.setInt(1, id);
                ResultSet results = database.executeQuery(statement);

                if (results != null) {
                    result = new Game(results.getInt("InstanceID"), results.getInt("UserID"), results.getDate("Date"), results.getString("PlayTime"));
                }
            }
        } catch (SQLException resultsException) {
            System.out.println("Database select by id error: " + resultsException.getMessage());
        }

        return result;
    }
}

