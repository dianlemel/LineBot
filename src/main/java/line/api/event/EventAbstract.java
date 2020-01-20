package line.api.event;

import java.util.Calendar;
import java.util.Date;
import java.util.Map;

import line.api.event.source.SourceAbstract;

public abstract class EventAbstract {

	private static final String TYPE = "type";
	private static final String SOURCE = "source";
	private static final String Timestamp = "timestamp";

	public static EventAbstract parseEvent(Map<String, Object> map) {
		EventType type = EventType.valueOf((String) map.get(TYPE));
		try {
			return type.getClassas().getConstructor(Map.class).newInstance(map);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private final double timestamp;
	private final SourceAbstract baseSource;

	@SuppressWarnings("unchecked")
	public EventAbstract(Map<String, Object> map) {
		super();
		this.timestamp = (double) map.get(Timestamp);
		baseSource = SourceAbstract.parse((Map<String, Object>) map.get(SOURCE));
	}

	public Date getTimestamp() {
		Calendar c = Calendar.getInstance();
		c.setTimeInMillis(new Double(timestamp).longValue());
		return c.getTime();
	}

	public SourceAbstract getBaseSource() {
		return baseSource;
	}

	public abstract EventType getType();
}
