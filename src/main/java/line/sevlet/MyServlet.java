package line.sevlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class MyServlet extends HttpServlet {

	private static final long serialVersionUID = -2729783035281106765L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		String path = req.getParameter("Path");
		System.out.println(path);
		if (path != null) {
			switch (path) {
			case "AddCourse":
				req.getRequestDispatcher("/AddCourse.jsp").forward(req, res);
				return;
			case "BeaconSetting":
				req.getRequestDispatcher("/BeaconSetting").forward(req, res);
				return;
			case "CreateCourse":
				req.getRequestDispatcher("/CreateCourse").forward(req, res);
				return;
			case "EditCourse":
				req.getRequestDispatcher("/EditCourse").forward(req, res);
				return;
			case "EditUser":
				req.getRequestDispatcher("/EditUser").forward(req, res);
				return;
			case "OnlineAttendance":
				req.getRequestDispatcher("/OnlineAttendance").forward(req, res);
				return;
			}
		}
		res.setStatus(302);
		res.setHeader("Location", "index.html");
	}

}
