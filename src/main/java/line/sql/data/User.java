package line.sql.data;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import com.google.gson.Gson;

import line.api.event.source.Profile;
import line.api.send.PushMessage;
import line.api.send.message.MessageAbstract;
import line.sevlet.LineBot;
import line.sql.dao.CourseUserDAO;
import line.util.Util;

public class User {

	private static Map<String, Map<String, Object>> data = new ConcurrentHashMap<>();
	private static Gson gson = new Gson();

	private int id;
	private String name;
	private String nickName;
	private String lineID;
	private String account;
	private UserType type;
	private String infomation;

	public User(String lineID, String name, String nickName) {
		super();
		this.name = name;
		this.nickName = nickName;
		this.lineID = lineID;
	}

	public User(int id, String name, String nickName, String lineID, String account, UserType type, String infomation) {
		super();
		this.id = id;
		this.name = name;
		this.nickName = nickName;
		this.lineID = lineID;
		this.account = account;
		this.type = type;
		this.infomation = infomation;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getInfomation() {
		return infomation;
	}

	public void setInfomation(String infomation) {
		this.infomation = infomation;
	}

	public UserType getType() {
		return type;
	}

	public void setType(String type) {
		setType(UserType.valueOf(type));
	}

	public void setType(UserType type) {
		switch (type) {
		case TEACHER:
		case ADMIN:
		case STUDENT:
			setRichMenu(this, type);
			break;
		default:
			clearRichMenu(this);
		}
		this.type = type;
	}

	public void setLineID(String lineID) {
		this.lineID = lineID;
	}

	public void pushMessage(List<MessageAbstract> baseMessages, boolean notification) {
		PushMessage pushMessage = new PushMessage(baseMessages, notification, lineID);
		pushMessage.send();
	}

	public void pushMessage(MessageAbstract baseMessage, boolean notification) {
		pushMessage(Arrays.asList(baseMessage), notification);
	}

	public List<Course> getCourse() {
		return CourseUserDAO.getInstance().getCourseUserByUser(this).stream().map(CourseUser::getCourse)
				.collect(Collectors.toList());
	}

	@SuppressWarnings("unchecked")
	public Profile getProfile() {
		try {
			HttpURLConnection con = LineBot.getHttpURLConnection("/profile/" + lineID);
			con.setDoInput(true);
			return new Profile(gson.fromJson(Util.InputStreamToString(con.getInputStream()), Map.class));
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public String getName() {
		return name;
	}

	public String getNickName() {
		return nickName;
	}

	public String getLineID() {
		return lineID;
	}

	public String getAccount() {
		return account;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((lineID == null) ? 0 : lineID.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		User other = (User) obj;
		if (lineID == null) {
			if (other.lineID != null)
				return false;
		} else if (!lineID.equals(other.lineID))
			return false;
		return true;
	}

	public Map<String, Object> getData() {
		return data.computeIfAbsent(lineID, V -> new ConcurrentHashMap<>());
	}

	private static void setRichMenu(User user, UserType type) {
		HttpURLConnection con = null;
		try {
			con = LineBot.getHttpURLConnection("/richmenu/bulk/link");
			System.out.println("URL: " + con.getURL());
			con.addRequestProperty("Content-Type", "application/json");
			con.setRequestMethod("POST");
			con.setDoOutput(true);
			try (OutputStream os = con.getOutputStream()) {
				List<String> users = new ArrayList<String>();
				users.add(user.getLineID());
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("richMenuId", type.richmenu);
				map.put("userIds", users);
				String send = gson.toJson(map);
				System.out.println("SEND: " + send);
				os.write(send.getBytes());
				os.flush();
			}
			if (con.getResponseCode() == 404) {
				System.out.println(Util.InputStreamToString(con.getErrorStream()));
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			Optional.ofNullable(con).ifPresent(HttpURLConnection::disconnect);
		}
	}

	private static void clearRichMenu(User user) {
		HttpURLConnection con = null;
		try {
			con = LineBot.getHttpURLConnection("/user/" + user.getLineID() + "/richmenu");
			con.setRequestMethod("DELETE");
			System.out.println("URL: " + con.getURL());
			con.getResponseCode();
		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			Optional.ofNullable(con).ifPresent(HttpURLConnection::disconnect);
		}
	}

	public static enum UserType {

		GUEST("richmenu-1aebaf77307b91e72e54e0d9aaefc3a4"), STUDENT("richmenu-4cb55b5ac1cba65dcd0163a4f5b679ec"),
		ADMIN("richmenu-43a1d6487ff782005355f946cbd5065d"), TEACHER("richmenu-99fbfd0a7e452f784a3fcc1b33a9f4ca");

		private final String richmenu;

		private UserType(String richmenu) {
			this.richmenu = richmenu;
		}
	}

}
