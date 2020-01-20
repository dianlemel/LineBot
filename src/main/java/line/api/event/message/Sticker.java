package line.api.event.message;

import java.util.Map;

public class Sticker extends MessageAbstract {

	private static final String PackageId = "packageId";
	private static final String StickerId = "stickerId";

	private final String packageId;
	private final String stickerId;

	public Sticker(Map<String, Object> map) {
		super(map);
		this.packageId = (String) map.get(PackageId);
		this.stickerId = (String) map.get(StickerId);
	}

	@Override
	public MessageType getType() {
		return MessageType.sticker;
	}

	public String getPackageId() {
		return packageId;
	}

	public String getStickerId() {
		return stickerId;
	}

}
