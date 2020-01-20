package line.api.send.message;

import java.util.Map;

public class Vidoe extends MessageAbstract {

	private static final String ORIGINAL_CONTENT_URL = "originalContentUrl";
	private static final String PERVIEW_IMAGE_URL = "previewImageUrl";

	/*
	 * URL of video file (Max: 1000 characters) HTTPS mp4 Max: 1 minute Max: 10 MB
	 */
	private final String originalContentUrl;

	/*
	 * Preview image URL (Max: 1000 characters) HTTPS JPEG Max: 240 x 240 Max: 1 MB
	 */
	private final String previewImageUrl;

	public Vidoe(String originalContentUrl, String previewImageUrl) {
		this.originalContentUrl = originalContentUrl;
		this.previewImageUrl = previewImageUrl;
	}

	@Override
	public MessageType getType() {
		return MessageType.video;
	}

	@Override
	public Map<String, Object> toMap() {
		Map<String, Object> map = super.toMap();
		map.put(ORIGINAL_CONTENT_URL, originalContentUrl);
		map.put(PERVIEW_IMAGE_URL, previewImageUrl);
		return map;
	}

}
