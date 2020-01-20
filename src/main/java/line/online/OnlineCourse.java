package line.online;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import com.google.gson.Gson;

import line.sql.data.Beacon;
import line.sql.data.Course;
import line.sql.data.CourseDate;
import line.sql.data.CourseUser;
import line.sql.data.CourseUserTime;
import line.sql.data.User;
import line.sql.data.CourseUser.CourseUserType;
import org.eclipse.jetty.websocket.api.Session;

public class OnlineCourse extends TimerTask {

	private static Gson gson = new Gson();
	public static final SimpleDateFormat TimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");

	private final Course course;
	private final CourseDate date;
	private final List<Session> sessions = new ArrayList<Session>();
	private Map<Integer, OnlineMember> members = new ConcurrentHashMap<>();
	private final List<Beacon> beacons;
	private Timer timer = new Timer();
	private byte level = 0;

	public OnlineCourse(Course course, CourseDate date) {
		super();
		this.course = course;
		beacons = course.getBeacons();
		this.date = date;
		course.getUsers(CourseUserType.STUDENT).parallelStream().map(CourseUser::getUser).forEach(user -> {
			members.put(user.getId(), new OnlineMember(user, this));
		});
	}

	public List<Beacon> getBeacons() {
		return beacons;
	}

	public CourseDate getDate() {
		return date;
	}

	public void start() {
		if (level == 1) {
			return;
		}
		level = 1;
		Map<String, Object> map = new HashMap<>();
		map.put("TYPE", "STATUS");
		map.put("DATA", 1);
		sendDate(map);
		timer.schedule(this, new Date(date.getEndDate()));
	}

	@Override
	public void run() {
		Course.cos.remove(course.getId());
		Map<String, Object> map = new HashMap<>();
		map.put("TYPE", "STATUS");
		map.put("DATA", 2);
		sendDate(map);
		level = 2;
		System.out.println("課程結束! 課程編號: " + course.getId() + " 課程名稱: " + course.getName());
	}

	public void sendDate(Map<String, Object> map) {
		if (level != 1) {
			return;
		}
		Iterator<Session> it = sessions.iterator();
		String send = gson.toJson(map);
		System.out.println("OnlineCourse Send: " + send);
		if (it.hasNext()) {
			do {
				Session session = it.next();
				try {
					session.getRemote().sendString(send);
				} catch (IOException e) {
					System.out.println("Error OnlineCourse: " + e.getMessage());
					it.remove();
				}
			} while (it.hasNext());
		}
	}

	public void addSession(Session session) {
		System.out.println("加入Websocket ，課程編號: " + course.getId() + " 課程名稱: " + course.getName());
		sessions.add(session);
	}

	public Course getCourse() {
		return course;
	}

	public void addUserTime(Beacon beacon, User user, long time, CourseUserTime.Type type) {
		members.computeIfAbsent(user.getId(), v -> {
			OnlineMember nom = new OnlineMember(user, OnlineCourse.this);
			Map<String, Object> map = new HashMap<>();
			map.put("TYPE", "ADD");
			map.put("DATA", nom.toMap());
			sendDate(map);
			return nom;
		}).addUserTime(beacon, user, time, type);
	}

	public String toJson() {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("NAME", course.getName());
		map.put("CLASSROOM", course.getClassRoom());
		long now = new Date().getTime();
		if (date.inRange(now)) {
			map.put("STATUS", 1);
		} else if (now < date.getStartDate()) {
			map.put("STATUS", 0);
		} else {
			map.put("STATUS", 2);
		}
		map.put("DATE", String.format("%s 至 %s", TimeFormat.format(new Date(date.getStartDate())),
				TimeFormat.format(new Date(date.getEndDate()))));
		map.put("MEMBER", members.values().stream().map(OnlineMember::toMap).collect(Collectors.toList()));
		return gson.toJson(map);
	}
}
