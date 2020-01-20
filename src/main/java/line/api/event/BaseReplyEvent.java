package line.api.event;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import line.api.send.ReplayMessage;
import line.api.send.message.MessageAbstract;

public abstract class BaseReplyEvent extends EventAbstract {

	private static final String REPLAY_TOKEN = "replyToken";
	private static final String REPLAY_TOKEN_INVALID_1 = "ffffffffffffffffffffffffffffffff";
	private static final String REPLAY_TOKEN_INVALID_2 = "00000000000000000000000000000000";

	private final String replyToken;

	public BaseReplyEvent(Map<String, Object> map) {
		super(map);
		this.replyToken = Optional.of(map.get(REPLAY_TOKEN)).map(String.class::cast).get();
	}

	public String getReplyToken() {
		return replyToken;
	}
	
	public boolean isInvalid() {
		return replyToken.equals(REPLAY_TOKEN_INVALID_1) || replyToken.equals(REPLAY_TOKEN_INVALID_2);
	}

	public void reply(MessageAbstract baseMessage, boolean notification){
		reply(Arrays.asList(baseMessage), notification);
	}

	public void reply(List<MessageAbstract> baseMessages, boolean notification) {
		ReplayMessage replayMessage = new ReplayMessage(baseMessages, notification, this);
		replayMessage.send();
	}

}
