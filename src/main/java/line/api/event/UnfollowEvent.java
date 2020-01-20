package line.api.event;

import java.util.Map;

public class UnfollowEvent extends EventAbstract {

	public UnfollowEvent(Map<String, Object> map) {
		super(map);
	}

	@Override
	public EventType getType() {
		return EventType.unfollow;
	}

}
