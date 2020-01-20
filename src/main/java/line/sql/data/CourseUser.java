package line.sql.data;

import line.sql.dao.CourseDAO;
import line.sql.dao.CourseUserDAO;
import line.sql.dao.UserDAO;

public class CourseUser {

	private int id = -1;
	private int idCourse;
	private int idUser;
	private CourseUserType type;

	public CourseUser(int id, int idCourse, int idUser, CourseUserType type) {
		super();
		this.id = id;
		this.idCourse = idCourse;
		this.idUser = idUser;
		this.type = type;
	}

	public CourseUser(int idCourse, int idUser, CourseUserType type) {
		super();
		this.idCourse = idCourse;
		this.idUser = idUser;
		this.type = type;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getCourseId() {
		return idCourse;
	}

	public void setCourseId(int idCourse) {
		this.idCourse = idCourse;
	}

	public Course getCourse() {
		return CourseDAO.getInstance().getCourseByID(idCourse);
	}

	public User getUser() {
		return UserDAO.getInstance().getUserByID(idUser);
	}

	public void delete() {
		if(id == -1) {
			return;
		}
		CourseUserDAO.getInstance().deleteCourseUser(this);
	}

	public int getUserId() {
		return idUser;
	}

	public void setUserId(int idUser) {
		this.idUser = idUser;
	}

	public CourseUserType getType() {
		return type;
	}

	public void setType(String type) {
		setType(CourseUserType.valueOf(type));
	}

	public void setType(CourseUserType type) {
		this.type = type;
	}

	public static enum CourseUserType {
		TEACHER, STUDENT
	}

}
