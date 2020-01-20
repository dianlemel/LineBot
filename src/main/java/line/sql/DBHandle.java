package line.sql;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Stream;

//import line.sql.SQLConnectionPool_OLD.CustomConnection;

public class DBHandle {

//    private CustomConnection connection;
//
//    public DBHandle(CustomConnection connection) {
//        this.connection = connection;
//    }
//
//    public void close() {
//        connection.close();
//    }
//    private static final String BACKUP_UPLOAD_SQL = "INSERT INTO Date(NFCID,DATE,CHECKPOINT,MODE) SELECT ?,?,?,?,? WHERE NOT EXISTS(SELECT 1 FROM Date WHERE NFCID = ? AND DATE = ? AND CHECKPOINT = ? AND MODE = ?);";
//
//    //處理手機備份資料
//    public void BACKUP_UPLOAD(ArrayList<Team> teams) throws Exception {
//        ArrayList<CheckPoint> checkpoints = GET_CHECKPOINTS();
//        connection.setAutoCommit(false);
//        PreparedStatement pst = connection.prepareStatement(BACKUP_UPLOAD_SQL);
//        class SQLException1 {
//
//            SQLException e = null;
//
//        }
//        SQLException1 e = new SQLException1();
//        teams.stream().forEach(team -> {
//            checkpoints.stream().forEach(cp -> {
//                Stream.of(Mode.values()).forEach(mode -> {
//                    if (!mode.equals(Mode.ALL) && team.getDate(cp.getQUEUE(), mode) != 0) {
//                        try {
//                            pst.setString(1, team.getNFCID());
//                            pst.setLong(2, team.getDate(0, Mode.NONE));
//                            pst.setInt(3, cp.getQUEUE());
//                            pst.setString(4, mode.toString());
//                            pst.executeUpdate();
//                        } catch (SQLException ex) {
//                            e.e = ex;
//                        }
//                    }
//                });
//            });
//        });
//        if (e.e != null) {
//            connection.rollback();
//            throw e.e;
//        }
//        connection.commit();
//    }
//    private static final String GET_TEAMS_SQL = new StringBuffer("SELECT * FROM ").append(Table.Team).append(" ORDER BY ").append(server.sql.table.Team.ID).toString();
//
//    //獲取隊伍清單
//    public ArrayList<Team> GET_TEAMS() throws Exception {
//        ArrayList<Team> teams = new ArrayList<>();
//        ResultSet rs = connection.executeQuery(GET_TEAMS_SQL);
//        if (rs.next()) {
//            do {
//                teams.add(new Team(
//                        rs.getString(server.sql.table.Team.ID.toString()),
//                        rs.getString(server.sql.table.Team.NAME.toString()),
//                        rs.getString(server.sql.table.Team.NFCID.toString()),
//                        rs.getInt(server.sql.table.Team.INDEX_ID.toString())
//                ));
//            } while (rs.next());
//        }
//        return teams;
//    }
//    private static final String GET_CHECKPOINTS_SQL = new StringBuilder("SELECT * FROM ").append(Table.CheckPoint).append(" ORDER BY ").append(server.sql.table.CheckPoint.QUEUE).toString();
//
//    //獲取檢查點清單
//    public ArrayList<CheckPoint> GET_CHECKPOINTS() throws Exception {
//        ArrayList<CheckPoint> checkPoints = new ArrayList<>();
//        ResultSet rs = connection.executeQuery(GET_CHECKPOINTS_SQL);
//        if (rs.next()) {
//            do {
//                checkPoints.add(new CheckPoint(
//                        rs.getInt(server.sql.table.CheckPoint.QUEUE.toString()),
//                        rs.getString(server.sql.table.CheckPoint.NAME.toString())
//                ));
//            } while (rs.next());
//        }
//        return checkPoints;
//    }
//
//    private static class Search {
//
//        private String name;
//
//        private Search setName(String name) {
//            this.name = name;
//            return this;
//        }
//
//        @Override
//        public boolean equals(Object object) {
//            if (!(object instanceof Team)) {
//                return false;
//            }
//            return ((Team) object).getNAME().equals(name);
//        }
//    }
//    private final Search search = new Search();
//
//    private static final String SERVER_HISTORY_CHECKPOINT_SQL = "SELECT Team.*,Date.MODE,Date.DATE FROM Date JOIN Team ON Date.NFCID = Team.NFCID WHERE Date.CHECKPOINT = %d";
//    private static final String SERVER_HISTORY_TEAM_ALL_SQL = "SELECT Team.*,Date.MODE,Date.DATE,Date.CHECKPOINT FROM Date JOIN Team ON Date.NFCID = Team.NFCID";
//    private static final String SERVER_HISTORY_TEAM_SOLE_SQL = "SELECT Team.*,Date.MODE,Date.DATE,Date.CHECKPOINT FROM Date JOIN Team ON Date.NFCID = Team.NFCID WHERE %s = '%s'";
//
//    //獲取紀錄
//    //ShowType -> st            TEAM,CHECKPOINT,NONE    顯示種類
//    //TeamQuantityType -> tqt   SOLE,ALL,NONE           搭配顯示種類<TEAM>顯示數量
//    //Condition -> c            NAME,ID,NONE            搭配顯示數量<SOLE>條件種類
//    //String -> d                                       搭配條件種類<NAME><ID>隊伍名稱&隊伍編號
//    //int -> q                                          搭配顯示種類<CHECKPOINT>檢查點順序
//    public ArrayList<Team> SERVER_HISTORY(ShowType st, TeamQuantityType tqt, Condition c, String d, int q) throws Exception {
//        ArrayList<Team> teams = new ArrayList<>();
//        ResultSet rs;
//        switch (st) {
//            case TEAM:
//                switch (tqt) {
//                    case SOLE:
//                        rs = connection.executeQuery(String.format(SERVER_HISTORY_TEAM_SOLE_SQL, "Team." + (c.equals(Condition.NAME) ? server.sql.table.Team.NAME.toString() : server.sql.table.Team.ID.toString()), d));
//                        if (rs.next()) {
//                            String name;
//                            int index = -1;
//                            int checkpoint = -1;
//                            Team team;
//                            Mode mode;
//                            long date;
//                            do {
//                                name = rs.getString(server.sql.table.Team.NAME.toString());
//                                checkpoint = rs.getInt(server.sql.table.Date.CHECKPOINT.toString());
//                                mode = Mode.valueOf(rs.getString(server.sql.table.Date.DATE.toString()));
//                                date = rs.getLong(server.sql.table.Date.DATE.toString());
//                                if ((index = teams.indexOf(search.setName(name))) < 0) {
//                                    team = new Team(
//                                            rs.getString(server.sql.table.Team.ID.toString()),
//                                            name,
//                                            rs.getString(server.sql.table.Team.NFCID.toString()),
//                                            rs.getInt(server.sql.table.Team.INDEX_ID.toString())
//                                    );
//                                    teams.add(team);
//                                } else {
//                                    team = teams.get(index);
//                                }
//                                if (team.getDate(checkpoint, mode) == 0 || date > team.getDate(checkpoint, mode)) {
//                                    team.addDate(checkpoint, date, mode);
//                                }
//                            } while (rs.next());
//                        }
//                        break;
//                    case ALL:
//                        rs = connection.executeQuery(SERVER_HISTORY_TEAM_ALL_SQL);
//                        if (rs.next()) {
//                            String name;
//                            int index;
//                            int checkpoint;
//                            Team team;
//                            Mode mode;
//                            long date;
//                            do {
//                                index = -1;
//                                name = rs.getString(server.sql.table.Team.NAME.toString());
//                                checkpoint = rs.getInt(server.sql.table.Date.CHECKPOINT.toString());
//                                mode = Mode.valueOf(rs.getString(server.sql.table.Date.DATE.toString()));
//                                date = rs.getLong(server.sql.table.Date.DATE.toString());
//                                if ((index = teams.indexOf(search.setName(name))) < 0) {
//                                    team = new Team(
//                                            rs.getString(server.sql.table.Team.ID.toString()),
//                                            name,
//                                            rs.getString(server.sql.table.Team.NFCID.toString()),
//                                            rs.getInt(server.sql.table.Team.INDEX_ID.toString())
//                                    );
//                                    teams.add(team);
//                                } else {
//                                    team = teams.get(index);
//                                }
//                                if (team.getDate(checkpoint, mode) == 0 || date > team.getDate(checkpoint, mode)) {
//                                    team.addDate(checkpoint, date, mode);
//                                }
//                            } while (rs.next());
//                        }
//                        break;
//                    default:
//                        throw new AssertionError(tqt.name());
//                }
//                break;
//            case CHECKPOINT:
//                rs = connection.executeQuery(String.format(SERVER_HISTORY_CHECKPOINT_SQL, q));
//                if (rs.next()) {
//                    String name;
//                    int index;
//                    Team team;
//                    Mode mode;
//                    long date;
//                    do {
//                        index = -1;
//                        name = rs.getString(server.sql.table.Team.NAME.toString());
//                        mode = Mode.valueOf(rs.getString(server.sql.table.Date.DATE.toString()));
//                        date = rs.getLong(server.sql.table.Date.DATE.toString());
//                        if ((index = teams.indexOf(search.setName(name))) < 0) {
//                            team = new Team(
//                                    rs.getString(server.sql.table.Team.ID.toString()),
//                                    name,
//                                    rs.getString(server.sql.table.Team.NFCID.toString()),
//                                    rs.getInt(server.sql.table.Team.INDEX_ID.toString())
//                            );
//                            teams.add(team);
//                        } else {
//                            team = teams.get(index);
//                        }
//                        if (team.getDate(q, mode) == 0 || date > team.getDate(q, mode)) {
//                            team.addDate(q, date, mode);
//                        }
//                    } while (rs.next());
//                }
//                break;
//            default:
//                throw new AssertionError(st.name());
//        }
//        return teams;
//    }
//    //增加隊伍感應紀錄
//    //String -> name        隊伍名稱
//    //String -> id          隊伍編號
//    //int -> checkpoint     檢查點順序
//    //Mode -> mode          感應模式
//    //long -> data          時間
//    //String -> seriaiid    手機編號
//    private static final String NFC_SENSOR_SQL = new StringBuffer("INSERT INTO ").append(Table.Date).append(" VALUES ('%s',%d,%d,'%s'").toString();
//
//    public void NFC_SENSOR(String name, String id, int checkpoint, Mode mode, long date) throws Exception {
//        connection.setAutoCommit(false);
//        try {
//            if (mode.equals(Mode.ALL)) {
//                for (Mode m : Mode.values()) {
//                    if (m.equals(Mode.ALL)) {
//                        continue;
//                    }
//                    connection.executeUpdate(String.format(NFC_SENSOR_SQL, name, date, checkpoint, m));
//                }
//            } else {
//                connection.executeUpdate(String.format(NFC_SENSOR_SQL, name, date, checkpoint, mode));
//            }
//        } finally {
//            connection.rollback();
//        }
//        connection.commit();
//    }
}
