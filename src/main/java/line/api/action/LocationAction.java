package line.api.action;

import java.util.Map;

public class LocationAction extends ActionAbstract {

	private static final String LABEL = "label";

	/*
	 * Max: 20 characters
	 */
	private String label;

	public LocationAction(String label) {
		super();
		this.label = label;
	}

	@Override
	public ActionType getActionType() {
		return ActionType.location;
	}

	@Override
	public Map<String, Object> toData() {
		Map<String, Object> item = super.toData();
		item.put(LABEL, label);
		return item;
	}

}
