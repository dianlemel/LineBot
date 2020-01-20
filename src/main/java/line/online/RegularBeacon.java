package line.online;

import line.sql.data.Beacon;
import line.sql.data.CourseUserTime.Type;

public class RegularBeacon implements BeaconTime {

	protected final Beacon beacon;
	protected final OnlineMember om;
	private Type type = Type.LEAVE;

	public RegularBeacon(Beacon beacon, OnlineMember om) {
		this.beacon = beacon;
		this.om = om;
	}

	@Override
	public void setType(Type type) {
		System.out.println("RegularBeacon SetType: " + type);
		this.type = type;
	}

	@Override
	public boolean inRange() {
		return type.equals(Type.ENTER);
	}

}
