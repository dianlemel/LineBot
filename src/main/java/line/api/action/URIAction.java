package line.api.action;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class URIAction extends ActionAbstract {

	private static final String LABEL = "label";
	private static final String URI = "uri";
	private static final String ALT_URI = "altUri";
	private static final String DESKTOP = "desktop";

	/*
	 * Required for templates other than image carousel. Max: 20 characters.
	 * 
	 * Optional for image carousel templates. Max: 12 characters.
	 * 
	 * Optional for rich menus. Spoken when the accessibility feature is enabled on
	 * the client device. Max: 20 characters. Supported on LINE 8.2.0 and later for
	 * iOS.
	 * 
	 * Required for the button of Flex Message. This property can be specified for
	 * the box, image, and text but its value is not displayed. Max: 20 characters.
	 */
	private Optional<String> label;
	/*
	 * Required
	 * 
	 * URI opened when the action is performed (Max: 1000 characters)
	 * 
	 * The available schemes are http, https, line, and tel. For more information
	 * about the LINE URL scheme, see
	 * https://developers.line.biz/en/docs/messaging-api/using-line-url-scheme/
	 */
	private String uri;
	/*
	 * Optional
	 * 
	 * URI opened on LINE for macOS and Windows when the action is performed (Max:
	 * 1000 characters)
	 * 
	 * If the altUri.desktop property is set, the uri property is ignored on LINE
	 * for macOS and Windows. The available schemes are http, https, line, and tel.
	 * For more information about the LINE URL scheme, see
	 * https://developers.line.biz/en/docs/messaging-api/using-line-url-scheme/ This
	 * property is supported on the following version of LINE.
	 * 
	 * LINE 5.12.0 or later for macOS and Windows
	 * 
	 * Note: The altUri.desktop property is supported only when you set URI actions
	 * in Flex Messages.
	 */
	private Optional<String> altUriDesktop;

	public URIAction(String label, String uri, String altUriDesktop) {
		super();
		this.label = Optional.ofNullable(label);
		this.uri = uri;
		this.altUriDesktop = Optional.ofNullable(altUriDesktop);
	}

	@Override
	public ActionType getActionType() {
		return ActionType.uri;
	}

	@Override
	public Map<String, Object> toData() {
		Map<String, Object> item = super.toData();
		item.put(URI, uri);
		altUriDesktop.ifPresent(v -> {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put(DESKTOP, altUriDesktop);
			item.put(ALT_URI, map);
		});
		label.ifPresent(v -> {
			item.put(LABEL, v);
		});
		return item;
	}

}
