package line.sevlet;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

import line.sql.dao.BeaconDAO;
import line.sql.dao.CourseDAO;
import line.sql.dao.UserDAO;
import line.sql.data.Course;
import line.sql.data.CourseUser.CourseUserType;
import line.util.Util;

public class CreateCourse extends HttpServlet {

	private static final long serialVersionUID = 1547614684330404434L;
	private static final Gson gson = new Gson();

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		res.setStatus(302);
		res.setHeader("Location", "CreateCourse.html");
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		res.setCharacterEncoding("UTF-8");
		System.out.println("CreateCourse Request: " + req.getParameter("json"));
		try {
			Map<String, Object> map = gson.fromJson(req.getParameter("json"), Map.class);
			switch ((String) map.get("TYPE")) {
			case "GET_USERS":
				List<Map<String, Object>> list = UserDAO.getInstance().getUsers().stream().map(user -> {
					Map<String, Object> data = new HashMap<>();
					data.put("ID", user.getId());
					data.put("NAME", user.getName());
					return data;
				}).collect(Collectors.toList());
				res.getWriter().print(gson.toJson(list));
				break;
			case "GET_CLASS_ROOM":
				res.getWriter().print(gson.toJson(BeaconDAO.getInstance().getClassRooms()));
				break;
			case "CREATE":
				Map<String, Object> data = (Map<String, Object>) map.get("DATA");
				String name = (String) data.get("NAME");
				String depiction = (String) data.get("DEPICTION");
				String classroom = (String) data.get("CLASSROOM");
				List<Double> students = (List<Double>) data.get("STUDENTS");
				List<Double> teachers = (List<Double>) data.get("TEACHERS");
				List<Map<String, Object>> dates = (List<Map<String, Object>>) data.get("DATE");
				Course course = new Course(name, depiction, classroom);
				CourseDAO.getInstance().createCourse(course);
				students.parallelStream().map(Double::intValue).map(UserDAO.getInstance()::getUserByID).forEach(user -> {
					course.addUser(user, CourseUserType.STUDENT, false);
				});
				teachers.parallelStream().map(Double::intValue).map(UserDAO.getInstance()::getUserByID).forEach(user -> {
					course.addUser(user, CourseUserType.TEACHER, false);
				});
				dates.parallelStream().forEach(date -> {
					double start = (double) date.get("START");
					double end = (double) date.get("END");
					course.addDate((long) start, (long) end);
				});
				course.checkTime();
				Util.generateQRCode(course);
				res.getWriter().print("ok");
				break;
			default:
				res.sendError(404);
				break;
			}
		} catch (Exception e1) {
			e1.printStackTrace();
		}
	}

}
