package line.api.action;

import java.util.HashMap;
import java.util.Map;

public abstract class ActionAbstract {
	
	private static final String TYPE = "type";
	private static final String ACTION = "action";
	
	public abstract ActionType getActionType();
	
	public Map<String, Object> toData(){
		Map<String, Object> item = new HashMap<String, Object>();
		item.put(TYPE, getActionType());
		return item;		
	}

	final public Map<String, Object> toMap() {
		Map<String, Object> item = new HashMap<String, Object>();
		item.put(TYPE, getActionType());
		item.put(ACTION, toData());
		return item;
	}
}
