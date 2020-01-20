package line.api.event;

public enum EventType {
	message(MessageEvent.class), follow(FollowEvent.class), unfollow(UnfollowEvent.class), join(JoinEvent.class),
	leave(LeaveEvent.class), memberJoined(MemberJoinEvent.class), memberLeft(MemberLeaveEvent.class), postback(PostBackEvent.class),
	beacon(BeaconEvent.class);

	private final Class<? extends EventAbstract> classas;

	private EventType(Class<? extends EventAbstract> classas) {
		this.classas = classas;
	}

	public Class<? extends EventAbstract> getClassas() {
		return classas;
	}

}
