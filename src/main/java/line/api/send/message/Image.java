package line.api.send.message;

import java.util.Map;

public class Image extends MessageAbstract {

	private static final String ORIGINAL_CONTENT_URL = "originalContentUrl";
	private static final String PERVIEW_IMAGE_URL = "previewImageUrl";

	/*
	 * Image URL (Max: 1000 characters) HTTPS JPEG Max: 1024 x 1024 Max: 1 MB
	 */
	private final String originalContentUrl;

	/*
	 * Preview image URL (Max: 1000 characters) HTTPS JPEG Max: 240 x 240 Max: 1 MB
	 */
	private final String previewImageUrl;
	
	public Image(String originalContentUrl, String previewImageUrl) {
		this.originalContentUrl = originalContentUrl;
		this.previewImageUrl = previewImageUrl;
	}

	@Override
	public MessageType getType() {
		return MessageType.image;
	}

	@Override
	public Map<String, Object> toMap() {
		Map<String, Object> map = super.toMap();
		map.put(ORIGINAL_CONTENT_URL, originalContentUrl);
		map.put(PERVIEW_IMAGE_URL, previewImageUrl);
		return map;
	}

}
