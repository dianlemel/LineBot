package line.sql;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.Queue;
import java.util.stream.Stream;

import line.sql.column.UserColumn;
import org.sqlite.SQLiteConfig;
import org.sqlite.SQLiteDataSource;

public class SQLConnectionPool {

	private static Queue<Connection> connections = new LinkedList<Connection>();
	private static final int PoolSize = 3;
	private static SQLiteDataSource dataSource;

	public static void init() throws SQLException {
		SQLiteConfig config = new SQLiteConfig();
		config.setSharedCache(true);
		config.enableRecursiveTriggers(true);
		dataSource = new SQLiteDataSource(config);
		dataSource.setUrl("jdbc:sqlite:" + System.getProperty("user.dir") + "/Database.db");
		Stream.of(Table.values()).forEach(CreateFunction::createTable);
	}
	
	public static interface CreateFunction{
		void createTable();
	}

	public static synchronized void closeConnection(Connection con) {
		if (connections.size() >= PoolSize) {
			try {
				con.close();
			} catch (SQLException e) {
			}
		} else {
			try {
				con.setAutoCommit(true);
			} catch (SQLException e1) {
			}
			connections.add(con);
		}
	}

	public static synchronized Connection getConnection() throws SQLException {
		if (!connections.isEmpty()) {
			Connection con = connections.poll();
			Statement stat = null;
			ResultSet res = null;
			try {
				stat = con.createStatement();
				res = stat.executeQuery("SELECT 1");
				return con;
			} catch (Exception e) {
			} finally {
				try {
					if (res != null) {
						res.close();
					}
				} catch (SQLException e) {
				}
				try {
					if (stat != null) {
						stat.close();
					}
				} catch (SQLException e) {
				}
			}
		}
		return dataSource.getConnection();
	}

}
