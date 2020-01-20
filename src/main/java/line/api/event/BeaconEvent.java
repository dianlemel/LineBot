package line.api.event;

import java.util.Map;
import java.util.Optional;

public class BeaconEvent extends BaseReplyEvent {

	private static final String HardwareID = "hwid";
	private static final String BeaconEventType = "type";
	private static final String DeviceMessage = "dm";
	private static final String Beacon = "beacon";

	private final String hwid;
	private final BeaconType beaconType;
	private final String dm;

	@SuppressWarnings("unchecked")
	public BeaconEvent(Map<String, Object> map) {
		super(map);
		Map<String, Object> beaconMap = (Map<String, Object>) map.get(Beacon);
		hwid = (String) beaconMap.get(HardwareID);
		beaconType = BeaconType.valueOf((String) beaconMap.get(BeaconEventType));
		dm = Optional.ofNullable(beaconMap.get(DeviceMessage)).map(String.class::cast).orElse("");
	}

	public String getHWID() {
		return hwid;
	}

	public BeaconType getBeaconType() {
		return beaconType;
	}

	public String getDm() {
		return dm;
	}

	@Override
	public EventType getType() {
		return EventType.beacon;
	}

	public static enum BeaconType {
		enter, leave, banner
	}

}
