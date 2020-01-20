package line.modle;

import java.util.Map;

import line.sql.data.User;

public abstract class ModleAbstract {

	private final String Key;

	public ModleAbstract(String Key) {
		super();
		this.Key = Key;
	}

	public final String getKey() {
		return Key;
	}

	public abstract void receive(User user, Map<String, Object> data, line.api.event.PostBackEvent event);

}
