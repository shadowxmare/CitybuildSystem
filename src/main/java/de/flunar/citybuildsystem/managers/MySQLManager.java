package de.flunar.citybuildsystem.managers;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MySQLManager {
    private String host;
    private int port;
    private String database;
    private String username;
    private String password;
    private Connection connection;
    private static final Logger logger = Logger.getLogger(MySQLManager.class.getName());

    public MySQLManager(String host, int port, String database, String username, String password) {
        this.host = host;
        this.port = port;
        this.database = database;
        this.username = username;
        this.password = password;
    }

    public boolean connect() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            String url = "jdbc:mysql://" + host + ":" + port + "/" + database + "?useSSL=false&serverTimezone=UTC";
            connection = DriverManager.getConnection(url, username, password);
            logger.info("Connected to MySQL database: " + database);
            return true;
        } catch (SQLException | ClassNotFoundException e) {
            logger.log(Level.SEVERE, "Failed to connect to MySQL database", e);
            return false;
        }
    }

    public boolean isConnected() {
        try {
            return connection != null && !connection.isClosed();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public void disconnect() {
        if (isConnected()) {
            try {
                connection.close();
                logger.info("Disconnected from MySQL database: " + database);
            } catch (SQLException e) {
                logger.log(Level.SEVERE, "Error while disconnecting from MySQL", e);
            }
        }
    }

    public ResultSet query(String sql) {
        if (!isConnected() && !connect()) {
            return null;
        }

        try {
            Statement statement = connection.createStatement();
            return statement.executeQuery(sql);
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error executing SQL query", e);
            return null;
        }
    }

    public int update(String sql) {
        if (!isConnected() && !connect()) {
            return -1;
        }

        try {
            Statement statement = connection.createStatement();
            return statement.executeUpdate(sql);
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error executing SQL update", e);
            return -1;
        }
    }

    public Connection getConnection() {
        if (!isConnected() && !connect()) {
            return null;
        }
        return connection;
    }

    public Location getSpawnLocation(int spawnId) {
        String query = "SELECT world, x, y, z, yaw, pitch FROM spawns_data WHERE spawns = ?;";
        try (PreparedStatement statement = getConnection().prepareStatement(query)) {
            statement.setInt(1, spawnId);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    String worldName = resultSet.getString("world");
                    World world = Bukkit.getWorld(worldName);
                    if (world == null) {
                        throw new SQLException("Welt " + worldName + " konnte nicht gefunden werden.");
                    }
                    double x = resultSet.getDouble("x");
                    double y = resultSet.getDouble("y");
                    double z = resultSet.getDouble("z");
                    float yaw = resultSet.getFloat("yaw");
                    float pitch = resultSet.getFloat("pitch");
                    return new Location(world, x, y, z, yaw, pitch);
                } else {
                    throw new SQLException("Kein Spawn-Punkt mit der ID " + spawnId + " gefunden.");
                }
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error retrieving spawn location", e);
            return null;
        }
    }

    public boolean checkIfPlayerExists(String uuid) throws SQLException {
        String query = "SELECT COUNT(*) FROM player_data WHERE uuid = ?";
        try (PreparedStatement stmt = getConnection().prepareStatement(query)) {
            stmt.setString(1, uuid);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        }
        return false;
    }

    public void addPlayer(String uuid, String playerName) throws SQLException {
        String query = "INSERT INTO player_data (uuid, player_name, firstjoin) VALUES (?, ?, NOW())";
        try (PreparedStatement stmt = getConnection().prepareStatement(query)) {
            stmt.setString(1, uuid);
            stmt.setString(2, playerName);
            stmt.executeUpdate();
        }
    }
}
