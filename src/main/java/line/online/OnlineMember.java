package line.online;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import line.LineEvent;
import line.sql.dao.CourseUserTimeDAO;
import line.sql.data.Beacon;
import line.sql.data.Beacon.Type;
import line.sql.data.CourseUserTime;
import line.sql.data.User;

public class OnlineMember {
	private final User user;
	private final OnlineCourse onlineCourse;
	private boolean check = false;
	private BeaconTime irregularBeaconTime;
	private BeaconTime regularBeaconTime;

	public OnlineMember(User user, OnlineCourse onlineCourse) {
		super();
		this.user = user;
		this.onlineCourse = onlineCourse;
		Map<Type, List<Beacon>> beacon = onlineCourse.getBeacons().stream()
				.collect(Collectors.groupingBy(b -> b.getType()));
		irregularBeaconTime = new IrregularBeacon(beacon.get(Type.IRREGULAR).get(0), this);
		regularBeaconTime = new RegularBeacon(beacon.get(Type.REGULAR).get(0), this);
	}

	public void addUserTime(Beacon beacon, User user, long time, CourseUserTime.Type type) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(time);
		System.out.printf("新增使用者時間，使用者名稱: %s ,BeaconHWID: %s ,BeaconType: %s ,教室名稱: %s ,時間: %s ,互動種類: %s\n",
				user.getName(), beacon.getHwid(), beacon.getType(), onlineCourse.getCourse().getName(),
				LineEvent.TimeFormat.format(calendar.getTime()), type.toString());
		CourseUserTime courseUserTime = new CourseUserTime(user, onlineCourse.getCourse(), onlineCourse.getDate(),
				beacon, time, type);
		CourseUserTimeDAO.getInstance().createCourseUserTime(courseUserTime);
		switch (beacon.getType()) {
		case IRREGULAR:
			irregularBeaconTime.setType(type);
			break;
		case REGULAR:
			regularBeaconTime.setType(type);
			break;
		default:
			System.out.println("ERROR: " + beacon.getType());
			return;
		}
		checkBeacon();
	}

	private boolean first = true;

	public void checkBeacon() {
		System.out.println("==========");
		boolean check = regularBeaconTime.inRange();
		System.out.println("RegularBeacon: " + regularBeaconTime.inRange());
		System.out.println("IrregularBeacon: " + irregularBeaconTime.inRange());
		if (!first) {
			check = check && irregularBeaconTime.inRange();
		}
		if (check) {
			first = false;
		}
		System.out.println("結果: " + check);
		System.out.println("==========");
		if (check == this.check) {
			return;
		}
		this.check = check;
		Map<String, Object> map = new HashMap<>();
		map.put("TYPE", "CHECK");
		map.put("DATA", user.getId());
		map.put("DATA2", check);
		onlineCourse.sendDate(map);
	}

	public Map<String, Object> toMap() {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("ID", user.getId());
		map.put("NAME", user.getName());
		map.put("ACCOUNT", user.getAccount());
		map.put("CHECK", check);
		return map;
	}
}
