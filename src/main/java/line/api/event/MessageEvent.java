package line.api.event;

import java.util.Map;

import line.api.event.message.MessageAbstract;

public class MessageEvent extends BaseReplyEvent {
	
	private static final String Message = "message";

	private final MessageAbstract message;

	@SuppressWarnings("unchecked")
	public MessageEvent(Map<String, Object> map) {
		super(map);
		this.message = MessageAbstract.parse((Map<String, Object>) map.get(Message));
	}

	public MessageAbstract getMessage() {
		return message;
	}

	@Override
	public EventType getType() {
		return EventType.message;
	}

}
