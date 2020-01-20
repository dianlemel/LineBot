package line;

import line.sevlet.AddCourse;
import line.sevlet.BeaconSetting;
import line.sevlet.CreateCourse;
import line.sevlet.EditCourse;
import line.sevlet.EditUser;
import line.sevlet.LineBot;
import line.sevlet.MyServlet;
import line.sevlet.OnlineAttendance;
import line.sql.SQLConnectionPool;
import line.sql.dao.CourseDAO;
import line.sql.data.Course;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.webapp.WebAppContext;

public class Main {

	public static final String SERVER_IP = "7b74e851.ap.ngrok.io";
	public static final String SERVER_QRCODE_URL = "https://" + SERVER_IP + "/qrcode/%s.jpg";

	public static void main(String[] args) throws Exception {
		Server server = new Server(8080);
		SQLConnectionPool.init();
		CourseDAO.getInstance().getClassRooms().parallelStream().forEach(Course::checkTime);
		WebAppContext webapp = new WebAppContext();
		webapp.setContextPath("/");
		webapp.setResourceBase(System.getProperty("user.dir") + "/WebContent/");
		webapp.setAttribute("org.eclipse.jetty.server.webapp.ContainerIncludeJarPattern", ".*/[^/]*jstl.*\\.jar$");
		org.eclipse.jetty.webapp.Configuration.ClassList classlist = org.eclipse.jetty.webapp.Configuration.ClassList
				.setServerDefault(server);
		classlist.addAfter("org.eclipse.jetty.webapp.FragmentConfiguration",
				"org.eclipse.jetty.plus.webapp.EnvConfiguration", "org.eclipse.jetty.plus.webapp.PlusConfiguration");
		classlist.addBefore("org.eclipse.jetty.webapp.JettyWebXmlConfiguration",
				"org.eclipse.jetty.annotations.AnnotationConfiguration");
		webapp.addServlet(OnlineAttendance.class, "/OnlineAttendance");
		LineBot bot = new LineBot();
		bot.addListener(LineEvent.class);
		webapp.addServlet(new ServletHolder(bot), "/LineBot");
		webapp.addServlet(AddCourse.class, "/AddCourse");
		webapp.addServlet(MyServlet.class, "/MyServlet");
		webapp.addServlet(CreateCourse.class, "/CreateCourse");
		webapp.addServlet(BeaconSetting.class, "/BeaconSetting");
		webapp.addServlet(EditCourse.class, "/EditCourse");
		webapp.addServlet(EditUser.class, "/EditUser");
		server.setHandler(webapp);
		server.start();
		server.join();
	}

}
