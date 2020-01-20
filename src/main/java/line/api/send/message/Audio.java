package line.api.send.message;

import java.util.Map;

public class Audio extends MessageAbstract {

	private static final String ORIGINAL_CONTENT_URL = "originalContentUrl";
	private static final String DURATION = "duration";

	/*
	 * URL of audio file (Max: 1000 characters) HTTPS m4a Max: 1 minute Max: 10 MB
	 */
	private final String originalContentUrl;

	/*
	 * milliseconds
	 */
	private final long duration = 60000;

	public Audio(String originalContentUrl) {
		this.originalContentUrl = originalContentUrl;
	}

	@Override
	public MessageType getType() {
		return MessageType.audio;
	}

	@Override
	public Map<String, Object> toMap() {
		Map<String, Object> map = super.toMap();
		map.put(ORIGINAL_CONTENT_URL, originalContentUrl);
		map.put(DURATION, duration);
		return map;
	}

}
