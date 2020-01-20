package line.api.action;

import java.util.Map;
import java.util.Optional;

import com.google.gson.Gson;

public class PostbackAction extends MessageAction {

	private static final Gson gson = new Gson();

	private static final String DATA = "data";
	private static final String DISPLAY_TEXT = "displayText";

	/*
	 * Max: 300 characters
	 */
	private String data;
	/*
	 * Max: 300 characters The displayText and text properties cannot both be used
	 * at the same time.
	 */
	private Optional<String> displayText;

	/*
	 * Max: 300 characters The displayText and text properties cannot both be used
	 * at the same time.
	 */
//	private String text;

	public PostbackAction(String label, String text, String data, String displayText) {
		super(label, text);
		this.displayText = Optional.ofNullable(displayText);
		this.data = data;
	}

	public PostbackAction(String label, String text, Map<String, Object> data, String displayText) {
		super(label, text);
		this.displayText = Optional.ofNullable(displayText);
		this.data = gson.toJson(data);
	}

	@Override
	public ActionType getActionType() {
		return ActionType.postback;
	}

	@Override
	public Map<String, Object> toData() {
		Map<String, Object> item = super.toData();
		item.put(DATA, data);
		displayText.ifPresent(v -> {
			item.put(DISPLAY_TEXT, v);
		});
		return item;
	}

}
