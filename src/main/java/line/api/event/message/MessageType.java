package line.api.event.message;

public enum MessageType {
	text(Text.class), image(Image.class), video(Video.class), audio(Audio.class), file(File.class), location(Location.class), sticker(Sticker.class);

	private final Class<? extends MessageAbstract> classas;

	private MessageType(Class<? extends MessageAbstract> classas) {
		this.classas = classas;
	}

	public Class<? extends MessageAbstract> getClassas() {
		return classas;
	}

}
