package line.api.send.message.template;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import line.api.action.ActionAbstract;

public class ConfirmTemplate extends TemplateAbstract {

	private static final String TEXT = "text";
	private static final String ACTIONS = "actions";

	/*
	 * Message text Max: 240 characters
	 */
	private final String text;
	/*
	 * Action when tapped Set 2 actions for the 2 buttons
	 */
	private final List<ActionAbstract> actions = new ArrayList<ActionAbstract>();

	public ConfirmTemplate(String text) {
		super();
		this.text = text;
	}

	@Override
	public Map<String, Object> toMap() {
		Map<String, Object> map = super.toMap();
		map.put(TEXT, text);
		map.put(ACTIONS, actions.stream().map(ActionAbstract::toData).collect(Collectors.toList()));
		return map;
	}

	public void addAction(ActionAbstract action) {
		actions.add(action);
	}

	@Override
	public TemplateType getType() {
		return TemplateType.confirm;
	}

}
