package line.api.event;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import line.api.event.source.SourceAbstract;
import line.api.event.source.SourceUser;

public class MemberJoinEvent extends BaseReplyEvent {

	private static final String Joined = "joined";
	private static final String Members = "members";

	private final List<SourceUser> users;

	@SuppressWarnings("unchecked")
	public MemberJoinEvent(Map<String, Object> map) {
		super(map);
		users = ((List<Map<String, Object>>) ((Map<String, Object>) map.get(Joined)).get(Members)).stream()
				.map(SourceAbstract::parse).map(SourceUser.class::cast).collect(Collectors.toList());
	}

	@Override
	public EventType getType() {
		return EventType.memberJoined;
	}

	public List<SourceUser> getUsers() {
		return users;
	}

}
