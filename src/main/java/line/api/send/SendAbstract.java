package line.api.send;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import com.google.gson.Gson;

import line.api.send.message.MessageAbstract;
import line.sevlet.LineBot;
import line.util.Util;

public abstract class SendAbstract {

	private final static Gson gson = new Gson();
	private final static String MESSAGEMS = "messages";
	private static final String NOTIFICATION_DISABLED = "notificationDisabled";

	/*
	 * Messages Max: 5
	 */
	private final List<MessageAbstract> baseMessages;
	/*
	 * true: The user doesn't receive a push notification when the message is sent.
	 * false: The user receives a push notification when the message is sent (unless
	 * they have disabled push notifications in LINE and/or their device).
	 */
	private final boolean notification;

	public SendAbstract(List<MessageAbstract> baseMessages, boolean notification) {
		this.baseMessages = baseMessages;
		this.notification = notification;
	}

	public SendAbstract(MessageAbstract baseMessage, boolean notification) {
		this.baseMessages = Arrays.asList(baseMessage);
		this.notification = notification;
	}

	public abstract String getURL();

	public Map<String, Object> toMap() {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put(MESSAGEMS, baseMessages.stream().map(MessageAbstract::toMap).collect(Collectors.toList()));
		map.put(NOTIFICATION_DISABLED, notification);
		return map;
	}

	public final void send() {
		baseMessages.removeAll(Collections.singleton(null));
		if (baseMessages.isEmpty()) {
			return;
		}
		if (baseMessages.size() > 5) {
			throw new RuntimeException("Messages Max: 5");
		}
		HttpURLConnection con = null;
		try {
			con = LineBot.getHttpURLConnection(getURL());
			System.out.println("URL: " + con.getURL());
			con.addRequestProperty("Content-Type", "application/json");
			con.setDoOutput(true);
			con.setDoInput(true);
			try (OutputStream os = con.getOutputStream()) {
				String send = gson.toJson(toMap());
				System.out.println("SEND: " + send);
				os.write(send.getBytes());
				os.flush();
			}
			if (con.getResponseCode() != 200) {
				throw new Exception(Util.InputStreamToString(con.getErrorStream()));
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			Optional.ofNullable(con).ifPresent(HttpURLConnection::disconnect);
		}

	}

}
