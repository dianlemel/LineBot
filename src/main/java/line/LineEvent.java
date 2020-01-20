package line;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import com.google.gson.Gson;

import line.api.action.PostbackAction;
import line.api.event.BeaconEvent.BeaconType;
import line.api.event.source.Profile;
import line.api.event.source.SourceUser;
import line.api.send.message.Sticker;
import line.api.send.message.Template;
import line.api.send.message.Text;
import line.api.send.message.template.ConfirmTemplate;
import line.modle.ModleAbstract;
import line.modle.postback.AccountLink;
import line.modle.postback.AdminToolOption;
import line.modle.postback.DefaultToolOption;
import line.modle.postback.Search;
import line.modle.postback.Setting;
import line.modle.postback.SettingInfomation;
import line.modle.postback.SettingName;
import line.sevlet.LineBot.EventHandle;
import line.sql.dao.BeaconDAO;
import line.sql.dao.CourseDAO;
import line.sql.dao.UserDAO;
import line.sql.data.Course;
import line.sql.data.CourseUserTime;
import line.sql.data.User;

public class LineEvent {

	public static final SimpleDateFormat TimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	@EventHandle
	public static void onMessageEvent(line.api.event.MessageEvent event) {
		System.out.println("==========MessageEvent==========");
		System.out.println("Date: " + TimeFormat.format(event.getTimestamp()));
		System.out.println("MessageType: " + event.getMessage().getType());
		User user = UserDAO.getInstance().getUserByLineID(event.getBaseSource().getUserId());
		switch (event.getMessage().getType()) {
		case audio:
			break;
		case file:
			break;
		case image:
			break;
		case location:
			break;
		case sticker:
			line.api.event.message.Sticker sticker = (line.api.event.message.Sticker) event.getMessage();
			event.reply(new Sticker(sticker.getPackageId(), sticker.getStickerId()), true);
			break;
		case text:
			line.api.event.message.Text text = (line.api.event.message.Text) event.getMessage();
			if (user.getData().containsKey("SETTING")) {
				switch ((String) user.getData().get("SETTING")) {
				case "SETTING_INFOMATION":
					ConfirmTemplate confirmTemplate = new ConfirmTemplate("個人敘述修改為:\n" + text.getText());
					Map<String, Object> data = new HashMap<String, Object>();
					data.put("TYPE", "SETTING_INFOMATION");
					data.put("DATA", "CONFIRM");
					data.put("DATA_2", text.getText());
					confirmTemplate.addAction(new PostbackAction("確認", null, gson.toJson(data), null));
					data = new HashMap<String, Object>();
					data.put("TYPE", "SETTING_INFOMATION");
					data.put("DATA", "CANCEL");
					confirmTemplate.addAction(new PostbackAction("取消", null, gson.toJson(data), null));
					Template rtext = new Template("請在手機上查看該訊息", confirmTemplate);
					event.reply(rtext, true);
					return;
				case "SETTING_NAME":
					ConfirmTemplate confirmTemplate2 = new ConfirmTemplate("確定個人名稱修改為: " + text.getText());
					Map<String, Object> data2 = new HashMap<String, Object>();
					data2.put("TYPE", "SETTING_NAME");
					data2.put("DATA", "CONFIRM");
					data2.put("DATA_2", text.getText());
					confirmTemplate2.addAction(new PostbackAction("確認", null, gson.toJson(data2), null));
					data2 = new HashMap<String, Object>();
					data2.put("TYPE", "SETTING_NAME");
					data2.put("DATA", "CANCEL");
					confirmTemplate2.addAction(new PostbackAction("取消", null, gson.toJson(data2), null));
					Template rtext2 = new Template("請在手機上查看該訊息", confirmTemplate2);
					event.reply(rtext2, true);
					return;
				}
			} else if (user.getData().containsKey("ACCOUNT_LINK")) {
				ConfirmTemplate confirmTemplate2 = new ConfirmTemplate("確認校園帳號綁定為: " + text.getText());
				Map<String, Object> data2 = new HashMap<String, Object>();
				data2.put("TYPE", "ACCOUNT_LINK");
				data2.put("DATA", "CONFIRM");
				data2.put("DATA_2", text.getText());
				confirmTemplate2.addAction(new PostbackAction("確認", null, gson.toJson(data2), null));
				data2 = new HashMap<String, Object>();
				data2.put("TYPE", "ACCOUNT_LINK");
				data2.put("DATA", "CANCEL");
				confirmTemplate2.addAction(new PostbackAction("取消", null, gson.toJson(data2), null));
				Template rtext2 = new Template("請在手機上查看該訊息", confirmTemplate2);
				event.reply(rtext2, true);
				return;
			} else {
				event.reply(new Text(text.getText()), true);
			}
			break;
		case video:
			break;
		default:
			break;
		}
	}

	private static final Gson gson = new Gson();
	private static List<ModleAbstract> postbackModles = new ArrayList<ModleAbstract>();

	static {
		postbackModles.add(new DefaultToolOption());
		postbackModles.add(new Setting());
		postbackModles.add(new Search());
		postbackModles.add(new SettingName());
		postbackModles.add(new SettingInfomation());
		postbackModles.add(new AccountLink());
		postbackModles.add(new AdminToolOption());
	}

	@SuppressWarnings("unchecked")
	@EventHandle
	public static void onPostBackEvent(line.api.event.PostBackEvent event) {
		System.out.println("==============PostBackEvent===============");
		System.out.println("RECEIVE: " + event.getData());
		Map<String, Object> data = gson.fromJson(event.getData(), Map.class);
		User user = UserDAO.getInstance().getUserByLineID(event.getBaseSource().getUserId());
		postbackModles.stream().filter(m -> m.getKey().equals(data.get("TYPE"))).findFirst()
				.ifPresent(m -> m.receive(user, data, event));
	}

	@SuppressWarnings("unchecked")
	@EventHandle
	public static void onBeaconEvent(line.api.event.BeaconEvent event) {
		System.out.println("==============BeaconEvent===============");
		System.out
				.println(TimeFormat.format(event.getTimestamp()) + " " + event.getHWID() + " " + event.getBeaconType());
		Optional.ofNullable(BeaconDAO.getInstance().getBeaconByHWID(event.getHWID())).ifPresent(beacon -> {
			User user = UserDAO.getInstance().getUserByLineID(event.getBaseSource().getUserId());
			List<String> beacons = (List<String>) user.getData().computeIfAbsent("Beacons",
					v -> Collections.synchronizedList(new ArrayList<String>()));
			if (event.getBeaconType().equals(BeaconType.enter)) {
				beacons.add(event.getHWID());
			} else {
				beacons.remove(event.getHWID());
			}
			List<Course> courses = CourseDAO.getInstance().getClassRoomByClassRoom(beacon.getClassRoom());
			if (courses.size() <= 0) {
				return;
			}
			long now = new Date().getTime();
			courses.parallelStream().filter(c -> c.inTime(now)).findFirst().ifPresent(course -> {
				if (course.isStudent(user)) {
					CourseUserTime.Type type = event.getBeaconType().equals(BeaconType.enter)
							? CourseUserTime.Type.ENTER
							: CourseUserTime.Type.LEAVE;
					course.getOnlineCourse().addUserTime(beacon, user, now, type);
				}
			});
		});
	}

	@EventHandle
	public static void onFollowEvent(line.api.event.FollowEvent event) {
		SourceUser sourceUser = (SourceUser) event.getBaseSource();
		Profile profile = sourceUser.getProfile();
		String lineID = event.getBaseSource().getUserId();
		User user = UserDAO.getInstance().getUserByLineID(lineID);
		if (user == null) {
			user = new User(lineID, profile.getDisplayName(), profile.getDisplayName());
			UserDAO.getInstance().createUser(user);
			event.reply(new Text("歡迎你使用校園助理LineBot，下方有選項工具可以進一步的選擇功能(請在手機版上查看)。"), true);
		}
	}

}
