package line.api.event.message;

import java.util.Map;

public class Text extends MessageAbstract {

	private static final String Text = "text";

	private final String text;

	public Text(Map<String, Object> map) {
		super(map);
		this.text = (String) map.get(Text);
	}

	public String getText() {
		return text;
	}

	@Override
	public MessageType getType() {
		return MessageType.text;
	}

}
