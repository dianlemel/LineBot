package line.sql;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Optional;

import line.sql.SQLConnectionPool.CreateFunction;

public enum Table implements CreateFunction {
	CourseDate {
		@Override
		public void createTable() {
			StringBuilder str = new StringBuilder();
			str.append("CREATE TABLE IF NOT EXISTS ").append(Table.CourseDate);
			str.append("(");
			str.append(line.sql.column.CourseDateColumn.ID).append(" INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,");
			str.append(line.sql.column.CourseDateColumn.ID_Course).append(" INTEGER NOT NULL,");
			str.append(line.sql.column.CourseDateColumn.StartDate).append(" INTEGER NOT NULL,");
			str.append(line.sql.column.CourseDateColumn.EndDate).append(" INTEGER NOT NULL");
			str.append(")");
			create(str.toString());
		}
	},
	Beacon {
		@Override
		public void createTable() {
			StringBuilder str = new StringBuilder();
			str.append("CREATE TABLE IF NOT EXISTS ").append(Table.Beacon);
			str.append("(");
			str.append(line.sql.column.BeaconColumn.ID).append(" INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,");
			str.append(line.sql.column.BeaconColumn.ClassRoom).append(" TEXT NOT NULL,");
			str.append(line.sql.column.BeaconColumn.HWID).append(" TEXT NOT NULL,");
			str.append(line.sql.column.BeaconColumn.Type).append(" TEXT NOT NULL");
			str.append(")");
			create(str.toString());
		}
	},
	CourseUserTime {
		@Override
		public void createTable() {
			StringBuilder str = new StringBuilder();
			str.append("CREATE TABLE IF NOT EXISTS ").append(Table.CourseUserTime);
			str.append("(");
			str.append(line.sql.column.CourseUserTimeColumn.ID).append(" INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,");
			str.append(line.sql.column.CourseUserTimeColumn.ID_Course).append(" INTEGER NOT NULL,");
			str.append(line.sql.column.CourseUserTimeColumn.ID_User).append(" INTEGER NOT NULL,");
			str.append(line.sql.column.CourseUserTimeColumn.ID_CourseDate).append(" INTEGER NOT NULL,");
			str.append(line.sql.column.CourseUserTimeColumn.ID_Beacon).append(" INTEGER NOT NULL,");
			str.append(line.sql.column.CourseUserTimeColumn.Date).append(" TEXT NOT NULL,");
			str.append(line.sql.column.CourseUserTimeColumn.Type).append(" TEXT NOT NULL");
			str.append(")");
			create(str.toString());
		}
	},
	CourseUser {
		@Override
		public void createTable() {
			StringBuilder str = new StringBuilder();
			str.append("CREATE TABLE IF NOT EXISTS ").append(Table.CourseUser);
			str.append("(");
			str.append(line.sql.column.CourseUserColumn.ID).append(" INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,");
			str.append(line.sql.column.CourseUserColumn.ID_Course).append(" INTEGER NOT NULL,");
			str.append(line.sql.column.CourseUserColumn.ID_User).append(" INTEGER NOT NULL,");
			str.append(line.sql.column.CourseUserColumn.Type).append(" TEXT NOT NULL");
			str.append(")");
			create(str.toString());
		}
	},
	Course {
		@Override
		public void createTable() {
			StringBuilder str = new StringBuilder();
			str.append("CREATE TABLE IF NOT EXISTS ").append(Table.Course);
			str.append("(");
			str.append(line.sql.column.CourseColumn.ID).append(" INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,");
			str.append(line.sql.column.CourseColumn.Name).append(" TEXT NOT NULL,");
			str.append(line.sql.column.CourseColumn.InviteCode).append(" TEXT NOT NULL,");
			str.append(line.sql.column.CourseColumn.Depiction).append(" TEXT NOT NULL,");
			str.append(line.sql.column.CourseColumn.ClassRoom).append(" TEXT NOT NULL");

			str.append(")");
			create(str.toString());
		}
	},
	User {
		@Override
		public void createTable() {
			StringBuilder str = new StringBuilder();
			str.append("CREATE TABLE IF NOT EXISTS ").append(Table.User);
			str.append("(");
			str.append(line.sql.column.UserColumn.ID).append(" INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,");
			str.append(line.sql.column.UserColumn.Name).append(" TEXT NOT NULL,");
			str.append(line.sql.column.UserColumn.NickName).append(" TEXT NOT NULL,");
			str.append(line.sql.column.UserColumn.LineID).append(" TEXT NOT NULL,");
			str.append(line.sql.column.UserColumn.Account).append(" TEXT,");
			str.append(line.sql.column.UserColumn.Type).append(" TEXT DEFAULT GUEST,");
			str.append(line.sql.column.UserColumn.Infomation).append(" TEXT DEFAULT 尚未設定");
			str.append(")");
			create(str.toString());
		}
	};

	public static void create(String sql) {
		Statement stat = null;
		Connection con = null;
		try {
			con = SQLConnectionPool.getConnection();
			stat = con.createStatement();
			stat.executeUpdate(sql);
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

}
