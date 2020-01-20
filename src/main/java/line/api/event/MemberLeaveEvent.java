package line.api.event;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import line.api.event.source.SourceAbstract;
import line.api.event.source.SourceUser;

public class MemberLeaveEvent extends EventAbstract {

	private static final String Left = "left";
	private static final String Members = "members";

	private final List<SourceUser> users;

	@SuppressWarnings("unchecked")
	public MemberLeaveEvent(Map<String, Object> map) {
		super(map);
		users = ((List<Map<String, Object>>) ((Map<String, Object>) map.get(Left)).get(Members)).stream()
				.map(SourceAbstract::parse).map(SourceUser.class::cast).collect(Collectors.toList());
	}

	@Override
	public EventType getType() {
		return EventType.memberLeft;
	}

	public List<SourceUser> getUsers() {
		return users;
	}

}
