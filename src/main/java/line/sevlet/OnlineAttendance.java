package line.sevlet;

import java.io.IOException;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import line.online.OnlineCourse;
import line.sql.dao.CourseDAO;
import line.sql.data.Course;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketClose;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import org.eclipse.jetty.websocket.servlet.WebSocketServlet;
import org.eclipse.jetty.websocket.servlet.WebSocketServletFactory;

@WebSocket
public class OnlineAttendance extends WebSocketServlet {

	private static final long serialVersionUID = 1L;
	private static final Map<String, Course> key = new ConcurrentHashMap<String, Course>();

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		if (Optional.of(req.getParameter("CourseId")).map(s -> !s.isEmpty()).isPresent()) {
			System.out.println("OnlineAttendance: " + req.getParameter("CourseId"));
			Course course;
			try {
				course = CourseDAO.getInstance().getCourseByID(Integer.parseInt(req.getParameter("CourseId")));
			} catch (NumberFormatException e) {
				resp.sendError(404);
				return;
			}
			if (Optional.ofNullable(course).isPresent()) {
				String key = UUID.randomUUID().toString();
				OnlineAttendance.key.put(key, course);
				req.setAttribute("Key", key);
				req.setAttribute("Data",
						Optional.ofNullable(course.getOnlineCourse()).map(OnlineCourse::toJson).orElse(""));
				req.getRequestDispatcher("/OnlineAttendance.jsp").forward(req, resp);
			} else {
				resp.sendError(404);
			}
		} else {
			resp.sendError(404);
		}

	}

	@Override
	public void configure(WebSocketServletFactory factory) {
		factory.register(OnlineAttendance.class);
	}

	@OnWebSocketMessage
	public void onText(Session session, String message) throws IOException {
		System.out.println("Message received: " + message);
		if (session.isOpen()) {
			System.out.println("onText: " + key.containsKey(message));
			Optional.of(key.get(message)).map(Course::getOnlineCourse).ifPresent(oc -> {
				oc.addSession(session);
				key.remove(message);
			});
		}
	}

	@OnWebSocketConnect
	public void onConnect(Session session) throws IOException {
		session.setIdleTimeout(1000 * 60 * 60 * 12);
		System.out.println(session.getRemoteAddress().getHostString() + " connected!");
	}

	@OnWebSocketClose
	public void onClose(Session session, int status, String reason) {
		System.out.println(session.getRemoteAddress().getHostString() + " closed!");
	}
}
