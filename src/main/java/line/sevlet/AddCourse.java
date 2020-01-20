package line.sevlet;

import java.io.IOException;
import java.util.Optional;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import line.api.send.message.Text;
import line.sql.dao.CourseDAO;
import line.sql.dao.UserDAO;
import line.sql.data.Course;
import line.sql.data.CourseUser.CourseUserType;

public class AddCourse extends HttpServlet {

	private static final long serialVersionUID = 1547614684330404434L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		req.getRequestDispatcher("/AddCourse.jsp").forward(req, res);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		System.out.println("AddCourse: " + req.getParameter("ID") + " " + req.getParameter("INVITE_CODE"));
		if (req.getParameter("ID") != null && req.getParameter("INVITE_CODE") != null) {
			String lineID = req.getParameter("ID");
			String inviteCode = req.getParameter("INVITE_CODE");
			Optional.ofNullable(UserDAO.getInstance().getUserByLineID(lineID)).ifPresent(user -> {
				Course course = CourseDAO.getInstance().getClassRoomByInviteCode(inviteCode);
				if (course == null) {
					user.pushMessage(new Text("查詢不到此教室邀請碼"), true);
				} else {
					if (course.hasUser(user)) {
						user.pushMessage(new Text("該課程學生名單中已經有你了"), true);
					} else {
						course.addUser(user, CourseUserType.STUDENT, true);
						user.pushMessage(new Text("成功加入 " + course.getName() + " 課程"), true);
					}
				}
			});
		}else {
			res.sendError(404);
		}
	}

}
