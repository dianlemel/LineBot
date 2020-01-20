package line.api.send.message;

import java.util.Map;

import line.api.send.message.template.TemplateAbstract;

public class Template extends MessageAbstract {

	private static final String ALT_TEXT = "altText";
	private static final String TEMPLATE = "template";

	private final String altText;
	private final TemplateAbstract templateAbstract;

	public Template(String altText, TemplateAbstract templateAbstract) {
		super();
		this.altText = altText;
		this.templateAbstract = templateAbstract;
	}

	@Override
	public MessageType getType() {
		return MessageType.template;
	}

	@Override
	public Map<String, Object> toMap() {
		Map<String, Object> map = super.toMap();
		map.put(ALT_TEXT, altText);
		map.put(TEMPLATE, templateAbstract.toMap());
		return map;
	}

}
