package line.api.event;

import java.util.Map;
import java.util.Optional;

public class PostBackEvent extends BaseReplyEvent {

	private static final String POST_BACK = "postback";
	private static final String DATA = "data";

	private final String data;

	public PostBackEvent(Map<String, Object> map) {
		super(map);
		Map<?, ?> postback = (Map<?, ?>) map.get(POST_BACK);
		data = Optional.ofNullable(postback.get(DATA)).map(String.class::cast).orElse("");
	}

	public String getData() {
		return data;
	}

	@Override
	public EventType getType() {
		return EventType.postback;
	}

}
