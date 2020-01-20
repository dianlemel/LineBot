package line.api.action;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Map;
import java.util.Optional;

public class DatetimePickerAction extends ActionAbstract {

	private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
	private static final SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
	private static final SimpleDateFormat datetimeFormat = new SimpleDateFormat("yyyy-MM-ddTHH:mm");

	private static String CalenderToText(Mode mode, Calendar calendar) {
		switch (mode) {
		case date:
			return dateFormat.format(calendar.getTime());
		case datetime:
			return datetimeFormat.format(calendar.getTime());
		case time:
			return timeFormat.format(calendar.getTime());
		}
		return null;
	}

	private static final String LABEL = "label";
	private static final String DATA = "data";
	private static final String MODE = "mode";
	private static final String INITAL = "initial";
	private static final String MAX = "max";
	private static final String MIN = "min";

	private Optional<String> label;
	/*
	 * Max: 300 characters
	 */
	private String data;
	private Mode mode;
	/*
	 * Initial value of date or time
	 */
	private Optional<Calendar> initial;
	/*
	 * Largest date or time value that can be selected. Must be greater than the min
	 * value.
	 */
	private Optional<Calendar> max;
	/*
	 * Smallest date or time value that can be selected. Must be less than the max
	 * value.
	 */
	private Optional<Calendar> min;

	public DatetimePickerAction(String label, String data, Mode mode, Calendar initial, Calendar max, Calendar min) {
		super();
		this.label = Optional.ofNullable(label);
		this.data = data;
		this.mode = mode;
		this.initial = Optional.ofNullable(initial);
		this.max = Optional.ofNullable(max);
		this.min = Optional.ofNullable(min);
	}

	@Override
	public ActionType getActionType() {
		return ActionType.datetimepicker;
	}

	@Override
	public Map<String, Object> toData() {
		Map<String, Object> item = super.toData();
		label.ifPresent(v -> {
			item.put(LABEL, v);
		});
		item.put(DATA, data);
		item.put(MODE, mode);
		initial.ifPresent(v -> {
			item.put(INITAL, CalenderToText(mode, v));
		});
		max.ifPresent(v -> {
			item.put(MAX, CalenderToText(mode, v));
		});
		min.ifPresent(v -> {
			item.put(MIN, CalenderToText(mode, v));
		});
		return item;
	}

	public static enum Mode {
		date, time, datetime
	}

}
