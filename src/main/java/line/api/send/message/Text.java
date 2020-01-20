package line.api.send.message;

import java.util.Map;

public class Text extends MessageAbstract {

	private static final String TEXT = "text";

	/*
	 * Max: 2000 characters
	 * Min: 1 characters
	 */
	private final String text;

	public Text(String text) {
		this.text = text;
	}

	@Override
	public Map<String, Object> toMap() {
		Map<String, Object> map = super.toMap();
		map.put(TEXT, text);
		return map;
	}

	@Override
	public MessageType getType() {
		return MessageType.text;
	}

}
