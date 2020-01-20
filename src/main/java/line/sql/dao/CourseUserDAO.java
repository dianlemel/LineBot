package line.sql.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import line.sql.SQLConnectionPool;
import line.sql.Table;
import line.sql.column.CourseUserColumn;
import line.sql.data.Course;
import line.sql.data.CourseUser;
import line.sql.data.User;
import line.sql.data.CourseUser.CourseUserType;

public class CourseUserDAO {

	private static CourseUserDAO dao = null;

	public static CourseUserDAO getInstance() {
		return Optional.ofNullable(dao).orElseGet(() -> dao = new CourseUserDAO());
	}

	private CourseUserDAO() {

	}

	private static final String GET_COURSE_USER_BY_ID = new StringBuilder("SELECT * FROM ").append(Table.CourseUser)
			.append(" WHERE ").append(CourseUserColumn.ID).append(" = %d").toString();

	public CourseUser getCourseUserByID(int id) {
		Statement stat = null;
		Connection con = null;
		ResultSet rs = null;
		try {
			con = SQLConnectionPool.getConnection();
			stat = con.createStatement();
			rs = stat.executeQuery(String.format(GET_COURSE_USER_BY_ID, id));
			if (rs.next()) {
				int i = rs.getInt(CourseUserColumn.ID.toString());
				int idCourse = rs.getInt(CourseUserColumn.ID_Course.toString());
				int idUser = rs.getInt(CourseUserColumn.ID_User.toString());
				CourseUserType type = CourseUserType.valueOf(rs.getString(CourseUserColumn.Type.toString()));
				return new CourseUser(i, idCourse, idUser, type);
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

	private static final String DELETE_COURSE_USER = new StringBuffer("DELETE FROM ").append(Table.CourseUser)
			.append(" WHERE ").append(CourseUserColumn.ID).append(" = %d").toString();

	public void deleteCourseUser(CourseUser courseUser) {
		Statement stat = null;
		Connection con = null;
		try {
			con = SQLConnectionPool.getConnection();
			stat = con.createStatement();
			stat.executeUpdate(String.format(DELETE_COURSE_USER, courseUser.getId()));
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

	private static final String GET_COURSE_USER_BY_COURSE = new StringBuffer("SELECT * FROM ").append(Table.CourseUser)
			.append(" WHERE ").append(CourseUserColumn.ID_Course).append(" = %d").toString();

	// 獲得該課程的所有成員
	public List<CourseUser> getCourseUserByCourse(Course course) {
		List<CourseUser> users = new ArrayList<CourseUser>();
		Statement stat = null;
		Connection con = null;
		ResultSet rs = null;
		try {
			con = SQLConnectionPool.getConnection();
			stat = con.createStatement();
			rs = stat.executeQuery(String.format(GET_COURSE_USER_BY_COURSE, course.getId()));
			while (rs.next()) {
				int i = rs.getInt(CourseUserColumn.ID.toString());
				int idCourse = rs.getInt(CourseUserColumn.ID_Course.toString());
				int idUser = rs.getInt(CourseUserColumn.ID_User.toString());
				CourseUserType type2 = CourseUserType.valueOf(rs.getString(CourseUserColumn.Type.toString()));
				CourseUser courseUser = new CourseUser(idCourse, idUser, type2);
				courseUser.setId(i);
				users.add(courseUser);
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

	private static final String GET_COURSE_USER_BY_COURSE_FOR_TEACHER = new StringBuffer("SELECT * FROM ")
			.append(Table.CourseUser).append(" WHERE ").append(CourseUserColumn.ID_Course).append(" = %d")
			.append(" AND ").append(CourseUserColumn.Type).append(" = '%s'").toString();

	// 獲得該課程的所有成員，針對種類
	public List<CourseUser> getCourseUserByCourseForType(Course course, CourseUserType type) {
		List<CourseUser> users = new ArrayList<CourseUser>();
		Statement stat = null;
		Connection con = null;
		ResultSet rs = null;
		try {
			con = SQLConnectionPool.getConnection();
			stat = con.createStatement();
			rs = stat.executeQuery(String.format(GET_COURSE_USER_BY_COURSE_FOR_TEACHER, course.getId(), type));
			while (rs.next()) {
				int i = rs.getInt(CourseUserColumn.ID.toString());
				int idCourse = rs.getInt(CourseUserColumn.ID_Course.toString());
				int idUser = rs.getInt(CourseUserColumn.ID_User.toString());
				CourseUserType type2 = CourseUserType.valueOf(rs.getString(CourseUserColumn.Type.toString()));
				CourseUser courseUser = new CourseUser(idCourse, idUser, type2);
				courseUser.setId(i);
				users.add(courseUser);
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

	private static final String GET_COURSE_USER_BY_USER = new StringBuffer("SELECT * FROM ").append(Table.CourseUser)
			.append(" WHERE ").append(CourseUserColumn.ID_User).append(" = %d").toString();

	// 獲得該使用者的所有課程
	public List<CourseUser> getCourseUserByUser(User user) {
		List<CourseUser> users = new ArrayList<CourseUser>();
		Statement stat = null;
		Connection con = null;
		ResultSet rs = null;
		try {
			con = SQLConnectionPool.getConnection();
			stat = con.createStatement();
			rs = stat.executeQuery(String.format(GET_COURSE_USER_BY_USER, user.getId()));
			while (rs.next()) {
				int i = rs.getInt(CourseUserColumn.ID.toString());
				int idCourse = rs.getInt(CourseUserColumn.ID_Course.toString());
				int idUser = rs.getInt(CourseUserColumn.ID_User.toString());
				CourseUserType type = CourseUserType.valueOf(rs.getString(CourseUserColumn.Type.toString()));
				CourseUser courseUser = new CourseUser(idCourse, idUser, type);
				courseUser.setId(i);
				users.add(courseUser);
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

	private static final String CREATE_COURSE = new StringBuffer("INSERT INTO ").append(Table.CourseUser).append("(")
			.append(CourseUserColumn.ID_Course).append(",").append(CourseUserColumn.ID_User).append(",")
			.append(CourseUserColumn.Type).append(") SELECT %d,%d,'%s'").toString();

	// 增加使用者課程
	public void createCourseUser(CourseUser courseUser) {
		Statement stat = null;
		Connection con = null;
		try {
			con = SQLConnectionPool.getConnection();
			stat = con.createStatement();
			stat.executeUpdate(String.format(CREATE_COURSE, courseUser.getCourseId(), courseUser.getUserId(),
					courseUser.getType()));
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

}
