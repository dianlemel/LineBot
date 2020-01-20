package line.api.send;

import java.util.List;
import java.util.Map;

import line.api.event.BaseReplyEvent;
import line.api.send.message.MessageAbstract;

public class ReplayMessage extends SendAbstract {

	private static final String REPLAY_URL = "/message/reply";
	private static final String REPLAY_TOKEN = "replyToken";

	private final BaseReplyEvent baseReplyEvent;

	public ReplayMessage(MessageAbstract baseMessages, boolean notification, BaseReplyEvent baseReplyEvent) {
		super(baseMessages, notification);
		this.baseReplyEvent = baseReplyEvent;
	}

	public ReplayMessage(List<MessageAbstract> baseMessages, boolean notification, BaseReplyEvent baseReplyEvent) {
		super(baseMessages, notification);
		this.baseReplyEvent = baseReplyEvent;
	}

	@Override
	public Map<String, Object> toMap() {
		Map<String, Object> map = super.toMap();
		map.put(REPLAY_TOKEN, baseReplyEvent.getReplyToken());
		return map;
	}

	@Override
	public String getURL() {
		return REPLAY_URL;
	}

}
