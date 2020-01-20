package line.sql.data;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import line.online.OnlineCourse;
import line.sql.dao.CourseDAO;
import line.sql.dao.CourseDateDAO;
import line.sql.dao.CourseUserTimeDAO;

public class CourseDate {

	private int id = -1;
	private int idCourse = -1;
	private long startDate;
	private long endDate;

	public CourseDate(Course course, Map<String, Object> map) {
		idCourse = course.getId();
		startDate = Optional.of(map.get("START")).map(Double.class::cast).map(Double::longValue).get();
		endDate = Optional.of(map.get("END")).map(Double.class::cast).map(Double::longValue).get();
	}

	public CourseDate(int idCourse, long startDate, long endDate) {
		super();
		this.idCourse = idCourse;
		this.startDate = startDate;
		this.endDate = endDate;
	}

	public CourseDate(int id, int idCourse, long startDate, long endDate) {
		super();
		this.id = id;
		this.idCourse = idCourse;
		this.startDate = startDate;
		this.endDate = endDate;
	}

	public boolean inRange(long time) {
		return startDate <= time && time < endDate;
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
	
	public Course getCourse() {
		return CourseDAO.getInstance().getCourseByID(idCourse);
	}

	public long getStartDate() {
		return startDate;
	}

	public long getEndDate() {
		return endDate;
	}

	public void delete() {
		if (id == -1 || idCourse == -1) {
			return;
		}	
		CourseUserTimeDAO.getInstance().deleteCourseUserTimesByCourseDate(this);
		CourseDateDAO.getInstance().deleteCourseDate(this);
		Optional.ofNullable(Course.cos.get(idCourse)).map(OnlineCourse::getCourse).ifPresent(Course::checkTime);	
	}

	public Map<String, Object> toMap() {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("START", startDate);
		map.put("END", endDate);
		map.put("ID", id);
		return map;
	}
}
