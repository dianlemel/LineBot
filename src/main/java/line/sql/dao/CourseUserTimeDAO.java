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
import line.sql.column.CourseUserTimeColumn;
import line.sql.data.CourseDate;
import line.sql.data.CourseUserTime;
import line.sql.data.CourseUserTime.Type;

public class CourseUserTimeDAO {
	private static CourseUserTimeDAO dao = null;

	public static CourseUserTimeDAO getInstance() {
		return Optional.ofNullable(dao).orElseGet(() -> dao = new CourseUserTimeDAO());
	}

	private CourseUserTimeDAO() {

	}

	private static final String DELETE_COURSE_USER_TIMES_BY_COURSE_DATE = new StringBuilder("DELETE FROM ")
			.append(Table.CourseUserTime).append(" WHERE ").append(CourseUserTimeColumn.ID_CourseDate).append(" = %d")
			.toString();

	public void deleteCourseUserTimesByCourseDate(CourseDate courseDate) {
		Statement stat = null;
		Connection con = null;
		try {
			con = SQLConnectionPool.getConnection();
			stat = con.createStatement();
			stat.executeUpdate(String.format(DELETE_COURSE_USER_TIMES_BY_COURSE_DATE, courseDate.getId()));
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

	private static final String GET_COURSE_USER_TIME = new StringBuilder("SELECT * FROM ").append(Table.CourseUserTime)
			.toString();

	public List<CourseUserTime> getCourseUserTimes() {
		List<CourseUserTime> times = new ArrayList<CourseUserTime>();
		Statement stat = null;
		Connection con = null;
		ResultSet rs = null;
		try {
			con = SQLConnectionPool.getConnection();
			stat = con.createStatement();
			rs = stat.executeQuery(GET_COURSE_USER_TIME);
			while (rs.next()) {
				int id = rs.getInt(CourseUserTimeColumn.ID.toString());
				int idUser = rs.getInt(CourseUserTimeColumn.ID_User.toString());
				int idCourse = rs.getInt(CourseUserTimeColumn.ID_Course.toString());
				int idCourseDate = rs.getInt(CourseUserTimeColumn.ID_CourseDate.toString());
				int idBeacon = rs.getInt(CourseUserTimeColumn.ID_Beacon.toString());
				long date = rs.getLong(CourseUserTimeColumn.Date.toString());
				Type type = Type.valueOf(rs.getString(CourseUserTimeColumn.Type.toString()));
				times.add(new CourseUserTime(id, idUser, idCourse, idCourseDate, idBeacon, date, type));
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
		return times;
	}

	private static final String CREATE_COURSE_USER_TIME = new StringBuilder("INSERT INTO ").append(Table.CourseUserTime)
			.append("(").append(CourseUserTimeColumn.ID_User).append(",").append(CourseUserTimeColumn.ID_Course)
			.append(",").append(CourseUserTimeColumn.ID_CourseDate).append(",").append(CourseUserTimeColumn.ID_Beacon)
			.append(",").append(CourseUserTimeColumn.Date).append(",").append(CourseUserTimeColumn.Type)
			.append(") SELECT %d,%d,%d,%d,'%d','%s'").toString();

	public void createCourseUserTime(CourseUserTime time) {
		Statement stat = null;
		Connection con = null;
		try {
			con = SQLConnectionPool.getConnection();
			stat = con.createStatement();
			stat.executeUpdate(String.format(CREATE_COURSE_USER_TIME, time.getIdUser(), time.getIdCourse(),time.getIdCourseDate(),
					time.getIdBeacon(), time.getDate(), time.getType()));
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
