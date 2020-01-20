package line.sql.data;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import line.LineEvent;
import line.online.OnlineCourse;
import line.sql.dao.BeaconDAO;
import line.sql.dao.CourseDateDAO;
import line.sql.dao.CourseUserDAO;
import line.sql.dao.UserDAO;
import line.sql.data.CourseUser.CourseUserType;

public class Course {

	public static Map<Integer, OnlineCourse> cos = new ConcurrentHashMap<>();
	public static Map<Integer, Timer> timer = new ConcurrentHashMap<>();

	private int id;
	private String name;
	private String inviteCode;
	private String depiction;
	private String classRoom;

	public Course(String name, String depiction, String classRoom) {
		super();
		this.name = name;
		this.inviteCode = UUID.randomUUID().toString();
		this.depiction = depiction;
		this.classRoom = classRoom;
	}

	public Course(String name, String inviteCode, String depiction, String classRoom) {
		super();
		this.name = name;
		this.inviteCode = inviteCode;
		this.depiction = depiction;
		this.classRoom = classRoom;
	}

	public Course(int id, String name, String inviteCode, String depiction, String classRoom) {
		super();
		this.id = id;
		this.name = name;
		this.inviteCode = inviteCode;
		this.depiction = depiction;
		this.classRoom = classRoom;
	}

	public void cancelTaak() {
		Optional.ofNullable(Course.timer.remove(id)).ifPresent(Timer::cancel);
	}

	public List<Beacon> getBeacons() {
		return BeaconDAO.getInstance().getBeaconByClassRoom(this);
	}

	public void checkTime() {
		Optional.ofNullable(Course.timer.get(id)).ifPresent(Timer::cancel);
		CourseDate date = CourseDateDAO.getInstance().getCourseDateByCalendar(this, Calendar.getInstance());
		if (Optional.ofNullable(date).isPresent()) {
			if (!cos.containsKey(id)) {
				cos.put(id, new OnlineCourse(this, date));
			}
			long now = new Date().getTime();
			if (now < date.getStartDate()) {
				cancelTaak();
				Date startDate = new Date(date.getStartDate());
				System.out.printf("課程編號: %d ,課程名稱: %s ,今日課程時間: %s\n", id, name, LineEvent.TimeFormat.format(startDate));
				Timer timer = new Timer(String.valueOf(id));
				timer.schedule(new TimerTask() {
					private CourseDate d = date;

					@Override
					public void run() {
						System.out.printf("課程編號: %d ,課程名稱: %s ，課程開始!\n", id, name);
						startCouser(d);
					}
				}, startDate);
				Course.timer.put(id, timer);
			} else if (date.inRange(now)) {
				System.out.printf("課程編號: %d ,課程名稱: %s ,課程正在進行中!\n", id, name);
				if (!cos.containsKey(id)) {
					startCouser(date);
				}
			}
		} else {
			if (cos.containsKey(id)) {
				System.out.printf("課程編號: %d ,課程名稱: %s ,該課程被移除!\n", id, name);
				cos.remove(id);
			}
		}
	}

	private void startCouser(CourseDate date) {
		getOnlineCourse().start();
		checkUsers();
		Optional.ofNullable(Course.timer.get(id)).ifPresent(Timer::cancel);
		Course.timer.remove(id);
	}

	public OnlineCourse getOnlineCourse() {
		return cos.get(id);
	}

	@SuppressWarnings("unchecked")
	private void checkBeaconForAddUserTime(User user) {
		List<String> beacons = (List<String>) user.getData().computeIfAbsent("Beacons",
				v -> Collections.synchronizedList(new ArrayList<String>()));
		long now = new Date().getTime();
		beacons.parallelStream().map(BeaconDAO.getInstance()::getBeaconByHWID).forEachOrdered(beacon -> {
			if (beacon.getClassRoom().equals(classRoom) && inTime(now)) {
				Optional.ofNullable(getOnlineCourse()).ifPresent(co -> {
					co.addUserTime(beacon, user, now, CourseUserTime.Type.ENTER);
				});
			}			
		});
	}

	public void checkUsers() {
		getUsers(CourseUserType.STUDENT).parallelStream().map(CourseUser::getUser)
				.forEach(this::checkBeaconForAddUserTime);
	}

	public void addUser(int userId, CourseUserType type, boolean checkTime) {
		CourseUser courseUser = new CourseUser(id, userId, type);
		CourseUserDAO.getInstance().createCourseUser(courseUser);
		if (checkTime) {
			checkBeaconForAddUserTime(UserDAO.getInstance().getUserByID(userId));
		}
	}

	public void addUser(User user, CourseUserType type, boolean checkTime) {
		CourseUser courseUser = new CourseUser(id, user.getId(), type);
		CourseUserDAO.getInstance().createCourseUser(courseUser);
		if (checkTime) {
			checkBeaconForAddUserTime(user);
		}
	}

	public boolean isTeacher(User user) {
		List<CourseUser> courseUsers = getUsers(CourseUserType.TEACHER);
		return courseUsers.stream().filter(c -> c.getUserId() == user.getId()).findFirst().isPresent();
	}

	public boolean isStudent(User user) {
		List<CourseUser> courseUsers = getUsers(CourseUserType.STUDENT);
		return courseUsers.stream().filter(c -> c.getUserId() == user.getId()).findFirst().isPresent();
	}

	public boolean hasUser(User user) {
		return getUsers(CourseUserType.STUDENT).stream().anyMatch(cu -> cu.getUserId() == user.getId());
	}

	public List<CourseUser> getUsers(CourseUserType type) {
		return CourseUserDAO.getInstance().getCourseUserByCourseForType(this, type);
	}

	public void addDate(long start, long end) {
		CourseDate courseDate = new CourseDate(id, start, end);
		CourseDateDAO.getInstance().createCourseDate(courseDate);
	}

	public boolean inTime(long time) {
		return getCourseDates().stream().anyMatch(cd -> cd.inRange(time));
	}

	public List<CourseDate> getCourseDates() {
		return CourseDateDAO.getInstance().getCourseDateByCourse(this);
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getId() {
		return id;
	}

	public String getDepiction() {
		return depiction;
	}

	public String getName() {
		return name;
	}

	public String getInviteCode() {
		return inviteCode;
	}

	public String getClassRoom() {
		return classRoom;
	}

	@Override
	public String toString() {
		return "Course [id=" + id + ", name=" + name + ", inviteCode=" + inviteCode + ", depiction=" + depiction
				+ ", classRoom=" + classRoom + "]";
	}

}
