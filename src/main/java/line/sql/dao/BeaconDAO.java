package line.sql.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import line.sql.SQLConnectionPool;
import line.sql.Table;
import line.sql.column.BeaconColumn;
import line.sql.data.Beacon;
import line.sql.data.Beacon.Type;
import line.sql.data.Course;

public class BeaconDAO {
	private static BeaconDAO dao = null;

	public static BeaconDAO getInstance() {
		return Optional.ofNullable(dao).orElseGet(() -> dao = new BeaconDAO());
	}

	private BeaconDAO() {

	}

	private static final String GET_BEACON_BY_HWID = new StringBuffer("SELECT * FROM ").append(Table.Beacon)
			.append(" WHERE ").append(BeaconColumn.HWID).append(" = '%s'").toString();

	public Beacon getBeaconByHWID(String hwid) {
		Statement stat = null;
		Connection con = null;
		ResultSet rs = null;
		try {
			con = SQLConnectionPool.getConnection();
			stat = con.createStatement();
			rs = stat.executeQuery(String.format(GET_BEACON_BY_HWID, hwid));
			while (rs.next()) {
				int id = rs.getInt(BeaconColumn.ID.toString());
				String cr = rs.getString(BeaconColumn.ClassRoom.toString());
				String h = rs.getString(BeaconColumn.HWID.toString());
				Type type = Type.valueOf(rs.getString(BeaconColumn.Type.toString()));
				return new Beacon(id, cr, h, type);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			Optional.ofNullable(rs).ifPresent(t -> {
				try {
					t.close();
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
			});
			;
			Optional.ofNullable(stat).ifPresent(s -> {
				try {
					s.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			});
			Optional.ofNullable(con).ifPresent(SQLConnectionPool::closeConnection);
		}
		return null;
	}

	private static final String DELETE_BEACON = new StringBuffer("DELETE FROM ").append(Table.Beacon).append(" WHERE ")
			.append(BeaconColumn.ID).append(" = %d").toString();

	public void deleteBeacon(int id) {
		Statement stat = null;
		Connection con = null;
		try {
			con = SQLConnectionPool.getConnection();
			stat = con.createStatement();
			stat.executeUpdate(String.format(DELETE_BEACON, id));
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			Optional.ofNullable(stat).ifPresent(s -> {
				try {
					s.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			});
			Optional.ofNullable(con).ifPresent(SQLConnectionPool::closeConnection);
		}
	}

	private static final String CREATE_BEACON = new StringBuffer("INSERT INTO ").append(Table.Beacon).append(" (")
			.append(BeaconColumn.ClassRoom).append(",").append(BeaconColumn.HWID).append(",").append(BeaconColumn.Type)
			.append(") SELECT '%s','%s','%s'").toString();

	public void createBeacon(String classroom, String hwid, String type) {
		Statement stat = null;
		Connection con = null;
		try {
			con = SQLConnectionPool.getConnection();
			stat = con.createStatement();
			stat.executeUpdate(String.format(CREATE_BEACON, classroom, hwid, type));
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			Optional.ofNullable(stat).ifPresent(s -> {
				try {
					s.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			});
			Optional.ofNullable(con).ifPresent(SQLConnectionPool::closeConnection);
		}
	}

	private static final String GET_BEACON_BY_CLASS_ROOM = new StringBuffer("SELECT * FROM ").append(Table.Beacon)
			.append(" WHERE ").append(BeaconColumn.ClassRoom).append(" = '%s'").toString();

	public List<Beacon> getBeaconByClassRoom(Course course) {
		return getBeaconByClassRoom(course.getClassRoom());
	}
	
	public List<Beacon> getBeaconByClassRoom(String classroom) {
		List<Beacon> beacons = new ArrayList<Beacon>();
		Statement stat = null;
		Connection con = null;
		ResultSet rs = null;
		try {
			con = SQLConnectionPool.getConnection();
			stat = con.createStatement();
			rs = stat.executeQuery(String.format(GET_BEACON_BY_CLASS_ROOM, classroom));
			while (rs.next()) {
				int id = rs.getInt(BeaconColumn.ID.toString());
				String cr = rs.getString(BeaconColumn.ClassRoom.toString());
				String hwid = rs.getString(BeaconColumn.HWID.toString());
				Type type = Type.valueOf(rs.getString(BeaconColumn.Type.toString()));
				Beacon beacon = new Beacon(id, cr, hwid, type);
				beacons.add(beacon);
			}
			return beacons;
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			Optional.ofNullable(rs).ifPresent(t -> {
				try {
					t.close();
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
			});
			;
			Optional.ofNullable(stat).ifPresent(s -> {
				try {
					s.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			});
			Optional.ofNullable(con).ifPresent(SQLConnectionPool::closeConnection);
		}
		return beacons;
	}

	private static final String GET_CLASS_ROOM = new StringBuffer("SELECT DISTINCT ").append(BeaconColumn.ClassRoom)
			.append(" FROM ").append(Table.Beacon).toString();

	public List<String> getClassRooms() {
		Statement stat = null;
		Connection con = null;
		ResultSet rs = null;
		List<String> classRooms = new ArrayList<String>();
		try {
			con = SQLConnectionPool.getConnection();
			stat = con.createStatement();
			rs = stat.executeQuery(GET_CLASS_ROOM);
			while (rs.next()) {
				classRooms.add(rs.getString(BeaconColumn.ClassRoom.toString()));
			}
			return classRooms;
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			Optional.ofNullable(rs).ifPresent(t -> {
				try {
					t.close();
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
			});
			;
			Optional.ofNullable(stat).ifPresent(s -> {
				try {
					s.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			});
			Optional.ofNullable(con).ifPresent(SQLConnectionPool::closeConnection);
		}
		return classRooms;
	}

}
