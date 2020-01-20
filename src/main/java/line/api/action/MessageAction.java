package line.api.action;

import java.util.Map;
import java.util.Optional;

public class MessageAction extends ActionAbstract {

	private static final String LABEL = "label";
	private static final String TEXT = "text";

	/*
	 * Required for templates other than image carousel. Max: 20 characters.
	 * 
	 * Optional for image carousel templates. Max: 12 characters.
	 * 
	 * Optional for rich menus. Spoken when the accessibility feature is enabled on
	 * the client device. Max: 20 characters. Supported on LINE 8.2.0 and later for
	 * iOS.
	 * 
	 * Required for quick reply buttons. Max: 20 characters. Supported on LINE
	 * 8.11.0 and later for iOS and Android.
	 * 
	 * Required for the button of Flex Message. This property can be specified for
	 * the box, image, and text but its value is not displayed. Max: 20 characters.
	 */
	private Optional<String> label;
	/*
	 * Text sent when the action is performed Max: 300 characters
	 */
	private String text;

	public MessageAction(String label, String text) {
		super();
		this.label = Optional.ofNullable(label);
		this.text = text;
	}

	@Override
	public ActionType getActionType() {
		return ActionType.message;
	}

	@Override
	public Map<String, Object> toData() {
		Map<String, Object> item = super.toData();
		item.put(TEXT, text);
		label.ifPresent(v -> {
			item.put(LABEL, v);
		});
		return item;
	}

}
