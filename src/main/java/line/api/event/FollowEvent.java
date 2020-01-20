package line.api.event;

import java.util.Map;

public class FollowEvent extends BaseReplyEvent {

	public FollowEvent(Map<String, Object> map) {
		super(map);
	}

	@Override
	public EventType getType() {
		return EventType.follow;
	}

}
