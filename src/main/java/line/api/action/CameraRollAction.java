package line.api.action;

import java.util.Map;

public class CameraRollAction extends ActionAbstract {

	private static final String LABEL = "label";

	/*
	 * Max: 20 characters
	 */
	private String label;

	public CameraRollAction(String label) {
		super();
		this.label = label;
	}

	@Override
	public ActionType getActionType() {
		return ActionType.cameraRoll;
	}

	@Override
	public Map<String, Object> toData() {
		Map<String, Object> item = super.toData();
		item.put(LABEL, label);
		return item;
	}

}
