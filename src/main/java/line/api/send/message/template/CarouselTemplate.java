package line.api.send.message.template;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import line.api.send.message.template.carousel.Column;
import line.api.send.message.template.carousel.ImageAspectRatio;
import line.api.send.message.template.carousel.ImageSize;

public class CarouselTemplate extends TemplateAbstract {

	private static final String IMAGE_ASPECT_RATIO = "imageAspectRatio";
	private static final String IMAGE_SIZE = "imageSize";
	private static final String COLUMS = "columns";

	/*
	 * Optional
	 * 
	 * Aspect ratio of the image. Specify one of the following values:
	 * 
	 * rectangle: 1.51:1 square: 1:1
	 */
	private ImageAspectRatio imageAspectRatio;
	/*
	 * Optional
	 * 
	 * Size of the image. Specify one of the following values:
	 * 
	 * cover: The image fills the entire image area. Parts of the image that do not
	 * fit in the area are not displayed.
	 * 
	 * contain: The entire image is displayed in the image area. A background is
	 * displayed in the unused areas to the left and right of vertical images and in
	 * the areas above and below horizontal images.
	 */
	private ImageSize imageSize;
	/*
	 * Required
	 * 
	 * Array of columns Max: 10
	 */
	private List<Column> columns = new ArrayList<Column>();

	public CarouselTemplate() {
		this(ImageAspectRatio.rectangle, ImageSize.cover);
	}

	public CarouselTemplate(ImageAspectRatio imageAspectRatio, ImageSize imageSize) {
		super();
		this.imageAspectRatio = imageAspectRatio;
		this.imageSize = imageSize;
	}

	public void addColumn(Column column) {
		columns.add(column);
	}

	@Override
	public Map<String, Object> toMap() {
		Map<String, Object> map = super.toMap();
		map.put(IMAGE_ASPECT_RATIO, imageAspectRatio);
		map.put(IMAGE_SIZE, imageSize);
		map.put(COLUMS, columns.stream().map(Column::toMap).collect(Collectors.toList()));
		return map;
	}

	@Override
	public TemplateType getType() {
		return TemplateType.carousel;
	}
}
