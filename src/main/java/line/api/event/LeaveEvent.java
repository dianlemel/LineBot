package line.api.event;

import java.util.Map;

public class LeaveEvent extends EventAbstract {

	public LeaveEvent(Map<String, Object> map) {
		super(map);
	}

	@Override
	public EventType getType() {
		return EventType.leave;
	}

}
