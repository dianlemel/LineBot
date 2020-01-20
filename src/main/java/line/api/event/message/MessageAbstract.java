package line.api.event.message;

import java.util.Map;

public abstract class MessageAbstract {

	private static final String Type = "type";

	public static MessageAbstract parse(Map<String, Object> map) {
		MessageType mType = MessageType.valueOf((String) map.get(Type));
		try {
			return mType.getClassas().getConstructor(Map.class).newInstance(map);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private static final String Id = "id";

	private final String id;

	public MessageAbstract(Map<String, Object> map) {
		super();
		this.id = (String) map.get(Id);
	}

	public String getId() {
		return id;
	}

	public abstract MessageType getType();
}
