package line.api.event.source;

import java.util.Map;
import java.util.Optional;

public abstract class SourceAbstract {

	private static final String UserId = "userId";
	private static final String Type = "type";

	public static SourceAbstract parse(Map<String, Object> map) {
		SourceType type = SourceType.valueOf((String) map.get(Type));
		try {
			return type.getClassas().getConstructor(Map.class).newInstance(map);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private final String userId;

	public SourceAbstract(Map<String, Object> map) {
		this.userId = Optional.ofNullable(map.get(UserId)).map(String.class::cast).orElse(null);
	}

	public abstract SourceType getType();

	final public String getUserId() {
		return userId;
	}

}
