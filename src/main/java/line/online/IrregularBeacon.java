package line.online;

import java.util.Optional;
import java.util.Timer;
import java.util.TimerTask;

import line.sql.data.Beacon;
import line.sql.data.CourseUserTime.Type;

public class IrregularBeacon implements BeaconTime {

	private static int RANGE = 15;

	protected final Beacon beacon;
	protected final OnlineMember om;
	private Timer timer = new Timer();
	private TimerTask task = null;
	private boolean inRange = false;

	public IrregularBeacon(Beacon beacon, OnlineMember om) {
		this.beacon = beacon;
		this.om = om;
	}

	@Override
	public void setType(Type type) {
		System.out.println("IrregularBeacon SetType: " + type);
		if (Optional.ofNullable(task).isPresent()) {
			System.out.println("IrregularBeacon 倒數任務已取消");
		}
		Optional.ofNullable(task).ifPresent(TimerTask::cancel);
		if (type.equals(Type.ENTER)) {
			inRange = true;
		} else {
			task = new TimerTask() {
				@Override
				public void run() {
					System.out.println("IrregularBeacon 時間到");
					inRange = false;
					om.checkBeacon();
				}
			};
			System.out.println("IrregularBeacon 開始倒數 " + RANGE + " 秒後到期");
			timer.schedule(task, RANGE * 1000);
		}
	}

	@Override
	public boolean inRange() {
		return inRange;
	}
}
