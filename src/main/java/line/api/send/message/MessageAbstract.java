package line.api.send.message;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import line.api.action.ActionAbstract;

public abstract class MessageAbstract {

	private static final String TYPE = "type";
	private static final String QUICK_REPLY = "quickReply";
	private static final String ITEMS = "items";

	// Max: 13 actions
	private List<ActionAbstract> actions = new ArrayList<ActionAbstract>();
	
	public void addAction(ActionAbstract action) {
		actions.add(action);
	}

	public abstract MessageType getType();

	public Map<String, Object> toMap() {
		Map<String, Object> message = new HashMap<String, Object>();
		message.put(TYPE, getType().toString());
		if (actions.size() > 0) {
			Map<String, Object> quickReply = new HashMap<String, Object>();
			quickReply.put(ITEMS, actions.stream().map(ActionAbstract::toMap).collect(Collectors.toList()));
			message.put(QUICK_REPLY, quickReply);
		}
		return message;
	}

}
