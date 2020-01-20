package line.sql.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Optional;

import line.sql.SQLConnectionPool;
import line.sql.Table;
import line.sql.column.CourseDateColumn;
import line.sql.data.Course;
import line.sql.data.CourseDate;

public class CourseDateDAO {

	private static CourseDateDAO dao;

	public static CourseDateDAO getInstance() {
		return Optional.ofNullable(dao).orElseGet(() -> dao = new CourseDateDAO());
	}

	private CourseDateDAO() {

	}

	private static final String DELETE_COURSE_DATE = new StringBuffer("DELETE FROM ").append(Table.CourseDate)
			.append(" WHERE ").append(CourseDateColumn.ID).append(" = %d").toString();

	public void deleteCourseDate(CourseDate courseDate) {
		Statement stat = null;
		Connection con = null;
		try {
			con = SQLConnectionPool.getConnection();
			stat = con.createStatement();
			stat.executeUpdate(String.format(DELETE_COURSE_DATE, courseDate.getId()));
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

	private static final String GET_COURSE_DATE_BY_ID = new StringBuilder("SELECT * FROM ").append(Table.CourseDate)
			.append(" WHERE ").append(CourseDateColumn.ID).append(" = %d").toString();

	public CourseDate getCourseDateByID(int id) {
		Statement stat = null;
		Connection con = null;
		ResultSet rs = null;
		try {
			con = SQLConnectionPool.getConnection();
			stat = con.createStatement();
			rs = stat.executeQuery(String.format(GET_COURSE_DATE_BY_ID, id));
			if (rs.next()) {
				int i = rs.getInt(CourseDateColumn.ID.toString());
				int courseID = rs.getInt(CourseDateColumn.ID_Course.toString());
				long startDate = rs.getLong(CourseDateColumn.StartDate.toString());
				long endDate = rs.getLong(CourseDateColumn.EndDate.toString());
				return new CourseDate(i, courseID, startDate, endDate);
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

	private static final String GET_COURSE_DATE_BY_CALENDAR = new StringBuilder("SELECT * FROM ")
			.append(Table.CourseDate).append(" WHERE ").append(CourseDateColumn.ID_Course).append(" = %d AND (")
			.append(CourseDateColumn.StartDate).append(" >= %d AND ").append(CourseDateColumn.StartDate)
			.append(" < %d)").toString();

	public CourseDate getCourseDateByCalendar(Course course, Calendar calendar) {
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		long startTime = calendar.getTimeInMillis();
		calendar.set(Calendar.HOUR_OF_DAY, 23);
		calendar.set(Calendar.MINUTE, 59);
		calendar.set(Calendar.SECOND, 59);
		long endTime = calendar.getTimeInMillis();
		Statement stat = null;
		Connection con = null;
		ResultSet rs = null;
		try {
			con = SQLConnectionPool.getConnection();
			stat = con.createStatement();
			rs = stat.executeQuery(String.format(GET_COURSE_DATE_BY_CALENDAR, course.getId(), startTime, endTime));
			if (rs.next()) {
				int id = rs.getInt(CourseDateColumn.ID.toString());
				int courseID = rs.getInt(CourseDateColumn.ID_Course.toString());
				long startDate = rs.getLong(CourseDateColumn.StartDate.toString());
				long endDate = rs.getLong(CourseDateColumn.EndDate.toString());
				return new CourseDate(id, courseID, startDate, endDate);
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

	private static final String GET_COURSE_DATE_BY_COURSE_ID = new StringBuffer("SELECT * FROM ")
			.append(Table.CourseDate).append(" WHERE ").append(CourseDateColumn.ID_Course).append(" = %d").toString();

	// 獲得該課程所有時間
	public List<CourseDate> getCourseDateByCourse(Course course) {
		List<CourseDate> dates = new ArrayList<CourseDate>();
		Statement stat = null;
		Connection con = null;
		ResultSet rs = null;
		try {
			con = SQLConnectionPool.getConnection();
			stat = con.createStatement();
			rs = stat.executeQuery(String.format(GET_COURSE_DATE_BY_COURSE_ID, course.getId()));
			while (rs.next()) {
				int id = rs.getInt(CourseDateColumn.ID.toString());
				int courseID = rs.getInt(CourseDateColumn.ID_Course.toString());
				long startDate = rs.getLong(CourseDateColumn.StartDate.toString());
				long endDate = rs.getLong(CourseDateColumn.EndDate.toString());
				dates.add(new CourseDate(id, courseID, startDate, endDate));
			}
			return dates;
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
		return dates;
	}

	private static final String CREATE_COURSE_DATE = new StringBuffer("INSERT INTO ").append(Table.CourseDate)
			.append("(").append(CourseDateColumn.ID_Course).append(",").append(CourseDateColumn.StartDate).append(",")
			.append(CourseDateColumn.EndDate).append(") SELECT %d,%d,%d").toString();

	// 建立該課程時間
	public void createCourseDate(CourseDate courseDate) {
		Statement stat = null;
		Connection con = null;
		try {
			con = SQLConnectionPool.getConnection();
			stat = con.createStatement();
			stat.executeUpdate(String.format(CREATE_COURSE_DATE, courseDate.getCourseId(), courseDate.getStartDate(),
					courseDate.getEndDate()));
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
