package line.api.event.source;

import java.util.Map;
import java.util.Optional;

public class SourceGroup extends SourceAbstract {

	private static final String GroupId = "groupId";

	private final String groupId;

	public SourceGroup(Map<String, Object> map) {
		super(map);
		this.groupId = Optional.ofNullable(map.get(GroupId)).map(String.class::cast).orElse("");
	}

	public String getGroupId() {
		return groupId;
	}

	@Override
	public SourceType getType() {
		return SourceType.group;
	}

}
