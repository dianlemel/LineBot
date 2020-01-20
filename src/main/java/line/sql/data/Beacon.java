package line.sql.data;

public class Beacon {
	private int id;
	private String classroom;
	private String hwid;
	private Type type;

	public Beacon(int id, String classroom, String hwid, Type type) {
		super();
		this.id = id;
		this.classroom = classroom;
		this.hwid = hwid;
		this.type = type;
	}

	public int getId() {
		return id;
	}

	public String getClassRoom() {
		return classroom;
	}

	public String getHwid() {
		return hwid;
	}

	public Type getType() {
		return type;
	}

	public static enum Type {
		IRREGULAR, REGULAR
	}
}
