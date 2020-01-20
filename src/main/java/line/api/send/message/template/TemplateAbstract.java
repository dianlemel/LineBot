package line.api.send.message.template;

import java.util.HashMap;
import java.util.Map;

public abstract class TemplateAbstract {

	private static final String TYPE = "type";

	public abstract TemplateType getType();

	public Map<String, Object> toMap() {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put(TYPE, getType());
		return map;
	}
}
