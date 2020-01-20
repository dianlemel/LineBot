package line.api.event.message;

import java.util.Map;

public class Image extends BaseContent {

	public Image(Map<String, Object> map) {
		super(map);
	}

	@Override
	public MessageType getType() {
		return MessageType.image;
	}

}
