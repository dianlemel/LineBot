package line.sql.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import line.sql.SQLConnectionPool;
import line.sql.Table;
import line.sql.column.UserColumn;
import line.sql.data.User;
import line.sql.data.User.UserType;

public class UserDAO {

	private static UserDAO dao = null;

	public static UserDAO getInstance() {
		return Optional.ofNullable(dao).orElseGet(() -> dao = new UserDAO());
	}

	private UserDAO() {

	}

	private static final String SET_ACCOUNT = new StringBuilder("UPDATE ").append(Table.User).append(" SET ")
			.append(UserColumn.Account).append(" = '%s' WHERE ").append(UserColumn.ID).append(" = %d")
			.append(" AND NOT EXISTS(SELECT 1 FROM ").append(Table.User).append(" WHERE ").append(UserColumn.Account)
			.append(" = '%s')").toString();

	public boolean setUserAccount(User user, String account) {
		Statement stat = null;
		Connection con = null;
		try {
			con = SQLConnectionPool.getConnection();
			stat = con.createStatement();
			System.out.println(String.format(SET_ACCOUNT, account, user.getId(), account));
			return stat.executeUpdate(String.format(SET_ACCOUNT, account, user.getId(), account)) > 0;
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			Optional.ofNullable(stat).ifPresent(s -> {
				try {
					s.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			});
			Optional.ofNullable(con).ifPresent(SQLConnectionPool::closeConnection);
		}
		return false;
	}

	private static final String UPDATE_USER = new StringBuffer("UPDATE ").append(Table.User).append(" SET ")
			.append(" %s ").append(" WHERE ").append(UserColumn.ID).append(" = %d").toString();

	public void updateUser(User user, Update... update) {
		Statement stat = null;
		Connection con = null;
		try {
			con = SQLConnectionPool.getConnection();
			stat = con.createStatement();
			String s = Arrays.stream(update).map(Update::toString).collect(Collectors.joining(", "));
			stat.executeUpdate(String.format(UPDATE_USER, s, user.getId()));
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			Optional.ofNullable(stat).ifPresent(s -> {
				try {
					s.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			});
			Optional.ofNullable(con).ifPresent(SQLConnectionPool::closeConnection);
		}
	}

	private static final String CREATE_USER = new StringBuffer("INSERT INTO ").append(Table.User).append("(")
			.append(UserColumn.Name).append(",").append(UserColumn.NickName).append(",").append(UserColumn.LineID)
			.append(") SELECT '%s','%s','%s'").toString();

	public void createUser(User user) {
		Statement stat = null;
		Connection con = null;
		try {
			con = SQLConnectionPool.getConnection();
			stat = con.createStatement();
			stat.executeUpdate(String.format(CREATE_USER, user.getName(), user.getNickName(), user.getLineID()));
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			Optional.ofNullable(stat).ifPresent(s -> {
				try {
					s.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			});
			Optional.ofNullable(con).ifPresent(SQLConnectionPool::closeConnection);
		}
	}

	private static final String GET_USERS = new StringBuffer("SELECT * FROM ").append(Table.User).toString();

	public List<User> getUsers() {
		List<User> users = new ArrayList<User>();
		Statement stat = null;
		Connection con = null;
		ResultSet rs = null;
		try {
			con = SQLConnectionPool.getConnection();
			stat = con.createStatement();
			rs = stat.executeQuery(GET_USERS);
			while (rs.next()) {
				String lineID = rs.getString(UserColumn.LineID.toString());
				int ID = rs.getInt(UserColumn.ID.toString());
				String Name = rs.getString(UserColumn.Name.toString());
				String NickName = rs.getString(UserColumn.NickName.toString());
				UserType type = Optional.ofNullable(rs.getString(UserColumn.Type.toString())).map(UserType::valueOf)
						.get();
				String Infomation = rs.getString(UserColumn.Infomation.toString());
				String Account = rs.getString(UserColumn.Account.toString());
				User user = new User(ID, Name, NickName, lineID, Account, type, Infomation);
				users.add(user);
			}
			return users;
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			Optional.ofNullable(rs).ifPresent(t -> {
				try {
					t.close();
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
			});
			Optional.ofNullable(stat).ifPresent(s -> {
				try {
					s.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			});
			Optional.ofNullable(con).ifPresent(SQLConnectionPool::closeConnection);
		}
		return users;
	}

	private static final String GET_USER_BY_ID = new StringBuffer("SELECT * FROM ").append(Table.User).append(" WHERE ")
			.append(UserColumn.ID).append(" = %d").toString();

	public User getUserByID(int id) {
		Statement stat = null;
		Connection con = null;
		ResultSet rs = null;
		try {
			con = SQLConnectionPool.getConnection();
			stat = con.createStatement();
			rs = stat.executeQuery(String.format(GET_USER_BY_ID, id));
			if (rs.next()) {
				String lineID = rs.getString(UserColumn.LineID.toString());
				int ID = rs.getInt(UserColumn.ID.toString());
				String Name = rs.getString(UserColumn.Name.toString());
				String NickName = rs.getString(UserColumn.NickName.toString());
				UserType type = Optional.ofNullable(rs.getString(UserColumn.Type.toString())).map(UserType::valueOf)
						.get();
				String Infomation = rs.getString(UserColumn.Infomation.toString());
				String Account = rs.getString(UserColumn.Account.toString());
				User user = new User(ID, Name, NickName, lineID, Account, type, Infomation);
				return user;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			Optional.ofNullable(rs).ifPresent(t -> {
				try {
					t.close();
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
			});
			;
			Optional.ofNullable(stat).ifPresent(s -> {
				try {
					s.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			});
			Optional.ofNullable(con).ifPresent(SQLConnectionPool::closeConnection);
		}
		return null;
	}

	private static final String GET_USER_BY_LINE_ID = new StringBuffer("SELECT * FROM ").append(Table.User)
			.append(" WHERE ").append(UserColumn.LineID).append(" = '%s'").toString();

	public User getUserByLineID(String lineID) {
		Statement stat = null;
		Connection con = null;
		ResultSet rs = null;
		try {
			con = SQLConnectionPool.getConnection();
			stat = con.createStatement();
			rs = stat.executeQuery(String.format(GET_USER_BY_LINE_ID, lineID));
			if (rs.next()) {
				int ID = rs.getInt(UserColumn.ID.toString());
				String Name = rs.getString(UserColumn.Name.toString());
				String NickName = rs.getString(UserColumn.NickName.toString());
				UserType type = Optional.ofNullable(rs.getString(UserColumn.Type.toString())).map(UserType::valueOf)
						.get();
				String Infomation = rs.getString(UserColumn.Infomation.toString());
				String Account = rs.getString(UserColumn.Account.toString());
				User user = new User(ID, Name, NickName, lineID, Account, type, Infomation);
				return user;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			Optional.ofNullable(rs).ifPresent(t -> {
				try {
					t.close();
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
			});
			;
			Optional.ofNullable(stat).ifPresent(s -> {
				try {
					s.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			});
			Optional.ofNullable(con).ifPresent(SQLConnectionPool::closeConnection);
		}
		return null;
	}

}
