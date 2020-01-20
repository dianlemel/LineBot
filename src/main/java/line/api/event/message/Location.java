package line.api.event.message;

import java.util.Map;

public class Location extends MessageAbstract {

	private static final String Title = "title";
	private static final String Address = "address";
	private static final String Latitude = "latitude";
	private static final String Longitude = "longitude";

	private final String title;
	private final String address;
	private final double latitude;
	private final double longitude;

	public Location(Map<String, Object> map) {
		super(map);
		this.title = (String) map.get(Title);
		this.address = (String) map.get(Address);
		this.latitude = (double) map.get(Latitude);
		this.longitude = (double) map.get(Longitude);
	}

	@Override
	public MessageType getType() {
		return MessageType.location;
	}

	public String getTitle() {
		return title;
	}

	public String getAddress() {
		return address;
	}

	public double getLatitude() {
		return latitude;
	}

	public double getLongitude() {
		return longitude;
	}

}
