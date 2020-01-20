package line.sevlet;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

import line.sql.column.CourseColumn;
import line.sql.dao.BeaconDAO;
import line.sql.dao.CourseDAO;
import line.sql.dao.CourseDateDAO;
import line.sql.dao.CourseUserDAO;
import line.sql.dao.Update;
import line.sql.dao.UserDAO;
import line.sql.data.Course;
import line.sql.data.CourseDate;
import line.sql.data.CourseUser;
import line.sql.data.CourseUser.CourseUserType;

public class EditCourse extends HttpServlet {

	private static final long serialVersionUID = 1547614684330404434L;
	private static final Gson gson = new Gson();

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {		
		String courseId = req.getParameter("CourseId");
		if (Optional.ofNullable(courseId).isPresent()) {
			res.setStatus(302);
			res.setHeader("Location", "EditCourse.html?CourseId=" + req.getParameter("CourseId"));
		} else {
			res.setStatus(404);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		res.setCharacterEncoding("UTF-8");
		System.out.println("EditCourse Request: "+req.getParameter("json"));
		Map<String, Object> map = gson.fromJson(req.getParameter("json"), Map.class);
		switch ((String) map.get("TYPE")) {
		case "CLASSSROOM":
			classRoom(map);
			break;
		case "GET":
			res.getWriter().print(getDate(map));
			break;
		case "NAME":
			name(map);
			break;
		case "DEPICTION":
			deptction(map);
			break;
		case "DATE":
			date(map);
			break;
		case "MEMBER":
			member(map);
			break;
		}
	}

	private void member(Map<String, Object> map) {
		switch ((String) map.get("DATA")) {
		case "ADD":
			int userid = Optional.of(map.get("DATA2")).map(Double.class::cast).map(Double::intValue).get();
			CourseUserType type = Optional.of(map.get("DATA3")).map(String.class::cast).map(CourseUserType::valueOf).get();
			Course course = CourseDAO.getInstance().getCourseByID(Integer.parseInt((String) map.get("DATA4")));
			course.addUser(userid, type, false);
			break;
		case "REMOVE":
			int id = Optional.of(map.get("DATA2")).map(Double.class::cast).map(Double::intValue).get();
			CourseUserDAO.getInstance().getCourseUserByID(id).delete();
			break;
		}
	}

	@SuppressWarnings("unchecked")
	private void date(Map<String, Object> map) {
		switch ((String) map.get("DATA")) {
		case "ADD":
			Course course = CourseDAO.getInstance().getCourseByID(Integer.parseInt((String) map.get("DATA3")));
			((List<Map<String, Object>>) map.get("DATA2")).parallelStream().map(m -> new CourseDate(course, m))
					.forEach(CourseDateDAO.getInstance()::createCourseDate);
			course.checkTime();
			break;
		case "REMOVE":
			int id = Optional.of(map.get("DATA2")).map(Double.class::cast).map(Double::intValue).get();
			CourseDateDAO.getInstance().getCourseDateByID(id).delete();
			break;
		}
	}

	private void deptction(Map<String, Object> map) {
		Course course = Optional.ofNullable(map.get("DATA")).map(String.class::cast).map(Integer::parseInt).map(CourseDAO.getInstance()::getCourseByID).get();
		String deptction = (String) map.get("DATA2");
		CourseDAO.getInstance().upDateCourse(course, new Update(CourseColumn.Depiction, deptction));
	}

	private void name(Map<String, Object> map) {
		Course course = Optional.ofNullable(map.get("DATA")).map(String.class::cast).map(Integer::parseInt).map(CourseDAO.getInstance()::getCourseByID).get();
		String name = (String) map.get("DATA2");
		CourseDAO.getInstance().upDateCourse(course, new Update(CourseColumn.Name, name));
	}

	private void classRoom(Map<String, Object> map) {
		Course course = Optional.ofNullable(map.get("DATA")).map(String.class::cast).map(Integer::parseInt).map(CourseDAO.getInstance()::getCourseByID).get();
		String classroom = (String) map.get("DATA2");
		CourseDAO.getInstance().upDateCourse(course, new Update(CourseColumn.ClassRoom, classroom));
	}

	private String getDate(Map<String, Object> map) {
		String data = (String) map.get("DATA");
		Course course = Optional.ofNullable(map.get("DATA2")).map(String.class::cast).map(Integer::parseInt).map(CourseDAO.getInstance()::getCourseByID).orElse(null);
		switch (data) {
		case "NAME":
			return course.getName();
		case "DEPICTION":
			return course.getDepiction();
		case "CLASSROOM_LIST":
			return gson.toJson(BeaconDAO.getInstance().getClassRooms());
		case "CLASSROOM":
			return course.getClassRoom();
		case "DATE":
			return gson.toJson(course.getCourseDates().stream().map(CourseDate::toMap).collect(Collectors.toList()));
		case "MEMEBER":
			Map<Integer, CourseUser> c = new HashMap<>();
			CourseUserDAO.getInstance().getCourseUserByCourse(course).stream().forEach(cu -> {
				c.put(cu.getUserId(), cu);
			});
			List<Map<String, Object>> member = UserDAO.getInstance().getUsers().stream().map(user -> {
				Map<String, Object> m = new HashMap<>();
				if (c.containsKey(user.getId())) {
					CourseUser courseUser = c.get(user.getId());
					m.put("ID", courseUser.getId());
					m.put("TYPE", courseUser.getType().toString());
				} else {
					m.put("ID", -1);
					m.put("TYPE", "NONE");
				}
				m.put("NAME", user.getName());
				m.put("USERID", user.getId());
				return m;
			}).collect(Collectors.toList());
			return gson.toJson(member);
		default:
			return "";
		}
	}

}
