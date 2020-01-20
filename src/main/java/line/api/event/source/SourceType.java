package line.api.event.source;

public enum SourceType {
	user(SourceUser.class), group(SourceGroup.class), room(SourceRoom.class);

	private final Class<? extends SourceAbstract> classas;

	private SourceType(Class<? extends SourceAbstract> classas) {
		this.classas = classas;
	}

	public Class<? extends SourceAbstract> getClassas() {
		return classas;
	}

}
