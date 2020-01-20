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
import line.sql.column.CourseColumn;
import line.sql.column.UserColumn;
import line.sql.data.Course;

public class CourseDAO {

	private static CourseDAO dao = null;

	public static CourseDAO getInstance() {
		return Optional.ofNullable(dao).orElseGet(() -> dao = new CourseDAO());
	}

	private CourseDAO() {

	}

	private static final String UPDATE_COURSE = new StringBuffer("UPDATE ").append(Table.Course).append(" SET ")
			.append(" %s ").append(" WHERE ").append(UserColumn.ID).append(" = %d").toString();

	public void upDateCourse(Course course, Update... update) {
		Statement stat = null;
		Connection con = null;
		try {
			con = SQLConnectionPool.getConnection();
			stat = con.createStatement();
			String s = Arrays.stream(update).map(Update::toString).collect(Collectors.joining(", "));
			stat.executeUpdate(String.format(UPDATE_COURSE, s, course.getId()));
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

	private static final String CREATE_COURSE = new StringBuffer("INSERT INTO ").append(Table.Course).append("(")
			.append(CourseColumn.Name).append(",").append(CourseColumn.InviteCode).append(",")
			.append(CourseColumn.Depiction).append(",").append(CourseColumn.ClassRoom)
			.append(") SELECT '%s','%s','%s','%s'").toString();
	private static final String GET_COURSE_ID_BY_INVITE_CODE = new StringBuffer("SELECT ").append(CourseColumn.ID)
			.append(" FROM ").append(Table.Course).append(" WHERE ").append(CourseColumn.InviteCode).append(" = '%s'")
			.toString();

	public void createCourse(Course course) {
		Statement stat = null;
		Connection con = null;
		ResultSet rs = null;
		try {
			con = SQLConnectionPool.getConnection();
			stat = con.createStatement();
			stat.executeUpdate(String.format(CREATE_COURSE, course.getName(), course.getInviteCode(),
					course.getDepiction(), course.getClassRoom()));
			rs = stat.executeQuery(String.format(GET_COURSE_ID_BY_INVITE_CODE, course.getInviteCode()));
			rs.next();
			course.setId(rs.getInt(CourseColumn.ID.toString()));
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

	}

	private static final String GET_COURSE_BY_ID = new StringBuffer("SELECT * FROM ").append(Table.Course)
			.append(" WHERE ").append(CourseColumn.ID).append(" = '%d'").toString();

	public Course getCourseByID(int id) {
		Statement stat = null;
		Connection con = null;
		ResultSet rs = null;
		try {
			con = SQLConnectionPool.getConnection();
			stat = con.createStatement();
			rs = stat.executeQuery(String.format(GET_COURSE_BY_ID, id));
			if (rs.next()) {
				String name = rs.getString(CourseColumn.Name.toString());
				String code = rs.getString(CourseColumn.InviteCode.toString());
				String depiction = rs.getString(CourseColumn.Depiction.toString());
				String classRoom = rs.getString(CourseColumn.ClassRoom.toString());
				Course course = new Course(name, code, depiction, classRoom);
				course.setId(id);
				return course;
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
	
	private static final String GET_CLASS_ROOMS = new StringBuilder("SELECT * FROM ").append(Table.Course).toString();
	
	public List<Course> getClassRooms() {
		List<Course> courses = new ArrayList<Course>();
		Statement stat = null;
		Connection con = null;
		ResultSet rs = null;
		try {
			con = SQLConnectionPool.getConnection();
			stat = con.createStatement();
			rs = stat.executeQuery(GET_CLASS_ROOMS);
			if (rs.next()) {
				int id = rs.getInt(CourseColumn.ID.toString());
				String name = rs.getString(CourseColumn.Name.toString());
				String code = rs.getString(CourseColumn.InviteCode.toString());
				String depiction = rs.getString(CourseColumn.Depiction.toString());
				String classRoom = rs.getString(CourseColumn.ClassRoom.toString());
				Course course = new Course(id, name, code, depiction, classRoom);
				courses.add(course);
			}
			return courses;
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
		return courses;		
	}

	private static final String GET_COURSE_BY_CLASSROOM = new StringBuffer("SELECT * FROM ").append(Table.Course)
			.append(" WHERE ").append(CourseColumn.ClassRoom).append(" = '%s'").toString();

	public List<Course> getClassRoomByClassRoom(String classroom) {
		List<Course> courses = new ArrayList<Course>();
		Statement stat = null;
		Connection con = null;
		ResultSet rs = null;
		try {
			con = SQLConnectionPool.getConnection();
			stat = con.createStatement();
			rs = stat.executeQuery(String.format(GET_COURSE_BY_CLASSROOM, classroom));
			if (rs.next()) {
				int id = rs.getInt(CourseColumn.ID.toString());
				String name = rs.getString(CourseColumn.Name.toString());
				String code = rs.getString(CourseColumn.InviteCode.toString());
				String depiction = rs.getString(CourseColumn.Depiction.toString());
				String classRoom = rs.getString(CourseColumn.ClassRoom.toString());
				Course course = new Course(id, name, code, depiction, classRoom);
				courses.add(course);
			}
			return courses;
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
		return courses;
	}

	private static final String GET_COURSE_BY_INVITE_CODE = new StringBuffer("SELECT * FROM ").append(Table.Course)
			.append(" WHERE ").append(CourseColumn.InviteCode).append(" = '%s'").toString();

	public Course getClassRoomByInviteCode(String inviteCode) {
		Statement stat = null;
		Connection con = null;
		ResultSet rs = null;
		try {
			con = SQLConnectionPool.getConnection();
			stat = con.createStatement();
			rs = stat.executeQuery(String.format(GET_COURSE_BY_INVITE_CODE, inviteCode));
			if (rs.next()) {
				int id = rs.getInt(CourseColumn.ID.toString());
				String name = rs.getString(CourseColumn.Name.toString());
				String code = rs.getString(CourseColumn.InviteCode.toString());
				String depiction = rs.getString(CourseColumn.Depiction.toString());
				String classRoom = rs.getString(CourseColumn.ClassRoom.toString());
				Course course = new Course(name, code, depiction, classRoom);
				course.setId(id);
				return course;
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
