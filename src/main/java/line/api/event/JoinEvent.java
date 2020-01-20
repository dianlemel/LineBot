package line.api.event;

import java.util.Map;

public class JoinEvent extends BaseReplyEvent {
	
	public JoinEvent(Map<String, Object> map) {
		super(map);
	}

	@Override
	public EventType getType() {
		return EventType.join;
	}

}
