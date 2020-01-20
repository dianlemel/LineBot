package line.api.send.message;

import java.util.Map;

public class Location extends MessageAbstract {

	private static final String TITLE = "title";
	private static final String ADDRESS = "address";
	private static final String LATITUDE = "latitude";
	private static final String LONGITUDE = "longitude";

	/*
	 * Max: 100 characters
	 */
	private final String title;
	/*
	 * Max: 100 characters
	 */
	private final String address;
	private final float latitude;
	private final float longitude;

	public Location(String title, String address, float latitude, float longitude) {
		super();
		this.title = title;
		this.address = address;
		this.latitude = latitude;
		this.longitude = longitude;
	}

	@Override
	public MessageType getType() {
		return MessageType.location;
	}

	@Override
	public Map<String, Object> toMap() {
		Map<String, Object> map = super.toMap();
		map.put(TITLE, title);
		map.put(ADDRESS, address);
		map.put(LATITUDE, latitude);
		map.put(LONGITUDE, longitude);
		return map;
	}

}
