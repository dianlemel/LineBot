package line.api.event.source;

import java.util.Map;
import java.util.Optional;

public class SourceRoom extends SourceAbstract {

	private static final String RoomId = "roomId";

	private final String roomId;

	public SourceRoom(Map<String, Object> map) {
		super(map);
		this.roomId = Optional.ofNullable(map.get(RoomId)).map(String.class::cast).orElse("");
	}

	public String getRoomId() {
		return roomId;
	}

	@Override
	public SourceType getType() {
		return null;
	}

}
