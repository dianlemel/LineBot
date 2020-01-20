package line.api.send;

import java.util.List;
import java.util.Map;

import line.api.send.message.MessageAbstract;

public class PushMessage extends SendAbstract {

	private static final String PUSH_URL = "/message/push";
	private static final String TO = "to";

	/*
	 * ID of the target recipient. Use a userId, groupId, or roomId value returned
	 * in a webhook event object. Do not use the LINE ID found on LINE.
	 */
	private final String to;

	public PushMessage(MessageAbstract baseMessages, boolean notification, String to) {
		super(baseMessages, notification);
		this.to = to;
	}

	public PushMessage(List<MessageAbstract> baseMessages, boolean notification, String to) {
		super(baseMessages, notification);
		this.to = to;
	}

	@Override
	public Map<String, Object> toMap() {
		Map<String, Object> map = super.toMap();
		map.put(TO, to);
		return map;
	}

	@Override
	public String getURL() {
		return PUSH_URL;
	}

}
