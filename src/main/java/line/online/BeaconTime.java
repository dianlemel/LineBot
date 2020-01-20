package line.online;

import line.sql.data.CourseUserTime;

public interface BeaconTime {

	public abstract void setType(CourseUserTime.Type type);

	public abstract boolean inRange();

}
