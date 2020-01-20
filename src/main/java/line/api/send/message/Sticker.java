package line.api.send.message;

import java.util.Map;

public class Sticker extends MessageAbstract {

	private static final String PACKAGE_ID = "packageId";
	private static final String STICK_ID = "stickerId";

	/*
	 * https://developers.line.biz/media/messaging-api/sticker_list.pdf
	 */
	private final String packageId;
	private final String stickerId;

	public Sticker(String packageId, String stickerId) {
		this.packageId = packageId;
		this.stickerId = stickerId;
	}

	@Override
	public Map<String, Object> toMap() {
		Map<String, Object> map = super.toMap();
		map.put(PACKAGE_ID, packageId);
		map.put(STICK_ID, stickerId);
		return map;
	}

	@Override
	public MessageType getType() {
		return MessageType.sticker;
	}

}
