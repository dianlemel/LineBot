package line.api.event.message;

import java.util.Map;

public class Video extends BaseContent {
	
	private static final String Duration = "duration";

	private final double duration;

	public Video(Map<String, Object> map) {
		super(map);
		this.duration = (double) map.get(Duration);
	}

	public double getDuration() {
		return duration;
	}

	@Override
	public MessageType getType() {
		return MessageType.video;
	}

}
