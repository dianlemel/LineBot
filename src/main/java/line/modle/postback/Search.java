package line.modle.postback;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.stream.Collectors;

import line.Main;
import line.api.action.PostbackAction;
import line.api.action.URIAction;
import line.api.send.message.Image;
import line.api.send.message.MessageAbstract;
import line.api.send.message.Template;
import line.api.send.message.Text;
import line.api.send.message.template.CarouselTemplate;
import line.api.send.message.template.carousel.Column;
import line.modle.ModleAbstract;
import line.sevlet.LineBot;
import line.sql.dao.CourseDAO;
import line.sql.dao.UserDAO;
import line.sql.data.Course;
import line.sql.data.CourseDate;
import line.sql.data.CourseUser;
import line.sql.data.User;
import line.sql.data.User.UserType;
import line.sql.data.CourseUser.CourseUserType;

public class Search extends ModleAbstract {

	public static final String KEY = "SEARCH";

	public Search() {
		super(KEY);
	}

	@Override
	public void receive(User user, Map<String, Object> data, line.api.event.PostBackEvent event) {
		switch ((String) data.get("DATA")) {
		case "PERSONAL_INFOMATION":
			personal_infomation(user, data, event);
			break;
		case "COURSE_LIST":
			course_list(user, event);
			break;
		case "COURSE":
			course(user, data, event);
			break;
		case "OTHER_PERSONAL_INFOMATION":
			other_personal_infomation(data, event);
			break;
		case "COURSE_NOW":
			course_now(user, data, event);
			break;
		case "COURSE_MORE_INFOMATION":
			course_more_infomation(user, data, event);
			break;
		}
	}

	private void course_now(User user, Map<String, Object> data, line.api.event.PostBackEvent event) {
		long now = new Date().getTime();
		Optional<Course> cOptional = user.getCourse().stream().filter(c -> c.inTime(now)).findFirst();
		if (cOptional.isPresent()) {
			Course course = cOptional.get();
			CarouselTemplate carouselTemplate = new CarouselTemplate();
			StringBuilder sb = new StringBuilder();
			sb.append("教室: ").append(course.getClassRoom()).append("\n");
			sb.append("堂數: ").append(course.getCourseDates().size()).append("\n");
			sb.append("學生人數: ").append(course.getUsers(CourseUserType.STUDENT).size());
			Column column = new Column(null, null, course.getName(), sb.toString(), null);
			if (user.getType().equals(UserType.ADMIN) || course.isTeacher(user)) {
				column.addAction(new URIAction("編輯課程",
						"line://app/" + LineBot.LIFF_ID + "?Path=EditCourse&CourseId=" + course.getId(), null));
				column.addAction(new URIAction("線上出席狀況",
						"line://app/" + LineBot.LIFF_ID + "?Path=OnlineAttendance&CourseId=" + course.getId(), null));
			}
			Map<String, Object> data2 = new HashMap<String, Object>();
			data2.put("TYPE", "SEARCH");
			data2.put("DATA", "COURSE_MORE_INFOMATION");
			data2.put("DATA_2", course.getId());
			column.addAction(new PostbackAction("更多選擇...", null, data2, null));
			carouselTemplate.addColumn(column);
			Template rtext = new Template("請在手機上查看該訊息", carouselTemplate);
			user.pushMessage(rtext, true);
		} else {
			Text text = new Text("目前沒有課程正在進行中");
			event.reply(text, true);
		}
	}

	private void course_more_infomation(User user, Map<String, Object> data, line.api.event.PostBackEvent event) {
		Text text = new Text("請選擇要查看的類型");
		Course course = Optional.of(data.get("DATA_2")).filter(Course.class::isInstance).map(Course.class::cast)
				.orElse(Optional.of(data.get("DATA_2")).map(Double.class::cast).map(Double::intValue)
						.map(CourseDAO.getInstance()::getCourseByID).get());
		Map<String, Object> data2 = new HashMap<String, Object>();
		data2.put("TYPE", "SEARCH");
		data2.put("DATA", "COURSE_MORE_INFOMATION");
		data2.put("DATA_2", course.getId());
		text.addAction(new PostbackAction("查看出席率", null, data, null));
		data = new HashMap<String, Object>();
		data.put("TYPE", "SEARCH");
		data.put("DATA", "COURSE");
		data.put("DATA_2", "DATE");
		data.put("DATA_3", course.getId());
		text.addAction(new PostbackAction("查看上課日期", null, data, null));
		data.put("TYPE", "SEARCH");
		data.put("DATA", "COURSE");
		data.put("DATA_2", "DDEPICTION");
		data.put("DATA_3", course.getId());
		text.addAction(new PostbackAction("查看敘述", null, data, null));
		if (!course.isTeacher(user)) {
			data = new HashMap<String, Object>();
			data.put("TYPE", "SEARCH");
			data.put("DATA", "COURSE");
			data.put("DATA_2", "TEACHER");
			data.put("DATA_3", course.getId());
			text.addAction(new PostbackAction("查看老師資訊", null, data, null));
		}
		data = new HashMap<String, Object>();
		data.put("TYPE", "SEARCH");
		data.put("DATA", "COURSE");
		data.put("DATA_2", "QRCODE");
		data.put("DATA_3", course.getId());
		text.addAction(new PostbackAction("課程QRCODE", null, data, null));
		event.reply(text, true);
	}

	private void other_personal_infomation(Map<String, Object> data, line.api.event.PostBackEvent event) {
		int id = Optional.of(data.get("DATA_2")).map(Double.class::cast).map(Double::intValue).get();
		event.reply(new Text(UserDAO.getInstance().getUserByID(id).getInfomation()), true);
	}

	private void course(User user, Map<String, Object> data, line.api.event.PostBackEvent event) {
		Course course = CourseDAO.getInstance().getCourseByID((int) (double) data.get("DATA_3"));
		switch ((String) data.get("DATA_2")) {
		case "ATTENDANCE":
			// 出席率
			break;
		case "DATE":
			course_date(course, event);
			break;
		case "TEACHER":
			course_teacher(user, course, event);
			break;
		case "QRCODE":
			course_qrcode(user, course, event);
			break;
		case "DDEPICTION":
			course_qrcode(course, event);
			break;
		}
	}

	public static final SimpleDateFormat Date = new SimpleDateFormat("yyyy/MM/dd");
	public static final SimpleDateFormat Time = new SimpleDateFormat("HH:mm");

	private void course_qrcode(Course course, line.api.event.PostBackEvent event) {
		event.reply(new Text("敘述:\n" + course.getDepiction()), true);
	}

	private void course_qrcode(User user, Course course, line.api.event.PostBackEvent event) {
		List<MessageAbstract> messages = new ArrayList<MessageAbstract>();
		messages.add(new Text("課程名稱 " + course.getName() + " QRCODE"));
		String path = String.format(Main.SERVER_QRCODE_URL, course.getInviteCode());
		messages.add(new Image(path, path));
		user.pushMessage(messages, true);
	}

	private void course_date(Course course, line.api.event.PostBackEvent event) {
		List<CourseDate> dates = course.getCourseDates();
		StringBuilder sb = new StringBuilder();
		sb.append("課程: ").append(course.getName()).append("\n");
		sb.append(dates.stream().map(date -> {
			StringBuilder sb2 = new StringBuilder();
			Calendar c = Calendar.getInstance();
			c.setTimeInMillis(date.getStartDate());
			sb2.append("日期: ");
			sb2.append(Date.format(c.getTime())).append("  時間: ");
			sb2.append(Time.format(c.getTime())).append(" ~ ");
			c.setTimeInMillis(date.getEndDate());
			sb2.append(Time.format(c.getTime()));
			return sb2.toString();
		}).collect(Collectors.joining("\n")));
		Text text = new Text(sb.toString());
		event.reply(text, true);
	}

	private void course_teacher(User user, Course course, line.api.event.PostBackEvent event) {
		List<User> teachers = course.getUsers(CourseUserType.TEACHER).parallelStream().map(CourseUser::getUser)
				.collect(Collectors.toList());
		CarouselTemplate carouselTemplate = new CarouselTemplate();
		for (User teacher : teachers) {
			Column column = new Column(teacher.getProfile().getPictureUrl(), null, null, "教師姓名: " + teacher.getName(),
					null);
			Map<String, Object> data = new HashMap<String, Object>();
			data.put("TYPE", "SEARCH");
			data.put("DATA", "OTHER_PERSONAL_INFOMATION");
			data.put("DATA_2", teacher.getId());
			column.addAction(new PostbackAction("查看敘述", null, data, null));
			carouselTemplate.addColumn(column);
		}
		Template rtext = new Template("請在手機上查看該訊息", carouselTemplate);
		user.pushMessage(rtext, true);
	}

	private void course_list(User user, line.api.event.PostBackEvent event) {
		ConcurrentLinkedQueue<Course> queue = new ConcurrentLinkedQueue<Course>(user.getCourse());
		if (queue.isEmpty()) {
			event.reply(new Text("您尚未有任何的課程"), true);
		} else {
			event.reply(new Text("您的課程如下:"), true);
			do {
				CarouselTemplate carouselTemplate = new CarouselTemplate();
				for (int i = 0; i < 10; i++) {
					Course course = queue.poll();
					if (course == null) {
						break;
					}
					StringBuilder sb = new StringBuilder();
					sb.append("教室: ").append(course.getClassRoom()).append("\n");
					sb.append("堂數: ").append(course.getCourseDates().size()).append("\n");
					sb.append("學生人數: ").append(course.getUsers(CourseUserType.STUDENT).size());
					Column column = new Column(null, null, course.getName(), sb.toString(), null);
					if (user.getType().equals(UserType.ADMIN) || course.isTeacher(user)) {
						column.addAction(new URIAction("編輯課程",
								"line://app/" + LineBot.LIFF_ID + "?Path=EditCourse&CourseId=" + course.getId(), null));
						column.addAction(new URIAction("線上出席狀況",
								"line://app/" + LineBot.LIFF_ID + "?Path=OnlineAttendance&CourseId=" + course.getId(),
								null));
					}
					Map<String, Object> data = new HashMap<String, Object>();
					data.put("TYPE", "SEARCH");
					data.put("DATA", "COURSE_MORE_INFOMATION");
					data.put("DATA_2", course.getId());
					column.addAction(new PostbackAction("更多選擇...", null, data, null));
					carouselTemplate.addColumn(column);
				}
				Template rtext = new Template("請在手機上查看該訊息", carouselTemplate);
				user.pushMessage(rtext, true);

			} while (!queue.isEmpty());
		}
	}

	private void personal_infomation(User user, Map<String, Object> data, line.api.event.PostBackEvent event) {
		StringBuilder sb = new StringBuilder("個人名稱: ");
		sb.append(user.getName()).append("\n");
		sb.append("暱稱: ").append(user.getNickName()).append("\n");
		sb.append("校園帳號: ").append(Optional.ofNullable(user.getAccount()).orElse("尚未綁定")).append("\n");
		sb.append("個人敘述:\n").append(user.getInfomation());
		Text text = new Text(sb.toString());
		event.reply(text, true);
	}

}
