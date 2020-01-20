package line.sql.dao;

import java.util.Optional;

public class Update {

	private Enum<?> column;
	private Object value;

	public Update(Enum<?> column, Object editValue) {
		super();
		this.column = column;
		this.value = editValue;
	}

	@Override
	public String toString() {
		return column + " = " + Optional.of(value).filter(v -> {
			return v instanceof String || v instanceof Enum<?>;
		}).map(v -> (Object) String.format("'%s'", v)).orElse(value);
	}

}
