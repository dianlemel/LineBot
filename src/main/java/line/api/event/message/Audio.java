package line.api.event.message;

import java.util.Map;

public class Audio extends BaseContent {

	private static final String Duration = "duration";

	private final double duration;

	public Audio(Map<String, Object> map) {
		super(map);
		this.duration = (double) map.get(Duration);
	}

	@Override
	public void savePreview(String path) throws Exception {

	}

	public double getDuration() {
		return duration;
	}

	@Override
	public MessageType getType() {
		return MessageType.video;
	}

}
