package line.sql.data;

import line.sql.dao.CourseDateDAO;

public class CourseUserTime {

	private int id;
	private int idUser;
	private int idCourse;
	private int idCourseDate;
	private int idBeacon;
	private long date;
	private Type type;

	public CourseUserTime(User usesr, Course course, CourseDate courseDate, Beacon beacon, long date, Type type) {
		super();
		this.idUser = usesr.getId();
		this.idCourse = course.getId();
		this.idCourseDate = courseDate.getId();
		this.idBeacon = beacon.getId();
		this.date = date;
		this.type = type;
	}

	public CourseUserTime(int id, int id_user, int idCourseDate, int id_course, int id_beacon, long date, Type type) {
		super();
		this.id = id;
		this.idUser = id_user;
		this.idCourse = id_course;
		this.idCourseDate = idCourseDate;
		this.idBeacon = id_beacon;
		this.date = date;
		this.type = type;
	}
	
	public int getIdCourseDate() {
		return idCourseDate;
	}
	
	public CourseDate getCourseDate() {
		return CourseDateDAO.getInstance().getCourseDateByID(idCourseDate);
	}

	public int getId() {
		return id;
	}

	public int getIdUser() {
		return idUser;
	}

	public int getIdCourse() {
		return idCourse;
	}

	public int getIdBeacon() {
		return idBeacon;
	}

	public long getDate() {
		return date;
	}

	public Type getType() {
		return type;
	}

	public static enum Type {
		ENTER, LEAVE
	}
}
