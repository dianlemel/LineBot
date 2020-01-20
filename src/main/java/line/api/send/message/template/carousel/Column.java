package line.api.send.message.template.carousel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import line.api.action.ActionAbstract;

public class Column {

	private static final String IMAGE_BACKGROUND_COLOR = "IMAGE_BACKGROUND_COLOR";
	private static final String TITLE = "title";
	private static final String TEXT = "text";
	private static final String DEFAULT_ACTION = "defaultAction";
	private static final String ACTION = "actions";
	private static final String THUMBNAIL_IMAGE_URL = "thumbnailImageUrl";

	/*
	 * Optional
	 * 
	 * Image URL (Max: 1000 characters) HTTPS over TLS 1.2 or later JPEG or PNG
	 * Aspect ratio: 1:1.51 Max width: 1024px Max: 1 MB
	 */
	private String thumbnailImageUrl;
	/*
	 * Optional
	 * 
	 * Background color of image. Specify a RGB color value. The default value is
	 * #FFFFFF (white).
	 */
	private String imageBackgroundColor;
	/*
	 * Optional
	 * 
	 * Title Max: 40 characters
	 */
	private String title;
	/*
	 * Required
	 * 
	 * Message text Max: 120 characters (no image or title) Max: 60 characters
	 * (message with an image or title)
	 */
	private String text;
	/*
	 * Optional
	 * 
	 * Action when image is tapped; set for the entire image, title, and text area
	 */
	private ActionAbstract defaultAction;
	/*
	 * Required
	 * 
	 * Action when tapped Max: 3
	 */
	private List<ActionAbstract> actions = new ArrayList<ActionAbstract>();

	public Column(String thumbnailImageUrl, String imageBackgroundColor, String title, String text,
			ActionAbstract defaultAction) {
		super();
		this.thumbnailImageUrl = thumbnailImageUrl;
		this.imageBackgroundColor = imageBackgroundColor;
		this.title = title;
		this.text = text;
		this.defaultAction = defaultAction;
	}
	
	public void addAction(ActionAbstract action) {
		actions.add(action);
	}

	public Map<String, Object> toMap() {
		Map<String, Object> map = new HashMap<String, Object>();
		Optional.ofNullable(imageBackgroundColor).ifPresent(v -> {
			map.put(IMAGE_BACKGROUND_COLOR, v);
		});
		Optional.ofNullable(thumbnailImageUrl).ifPresent(v -> {
			map.put(THUMBNAIL_IMAGE_URL, v);
		});
		Optional.ofNullable(title).ifPresent(v -> {
			map.put(TITLE, v);
		});
		map.put(TEXT, text);
		Optional.ofNullable(defaultAction).map(ActionAbstract::toData).ifPresent(v -> {
			map.put(DEFAULT_ACTION, v);
		});
		map.put(ACTION, actions.stream().map(ActionAbstract::toData).collect(Collectors.toList()));
		return map;
	}

}
