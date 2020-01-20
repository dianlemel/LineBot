package line.sevlet;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

import line.sql.dao.BeaconDAO;

public class BeaconSetting extends HttpServlet {

	private static final long serialVersionUID = 1547614684330404434L;
	private static final Gson gson = new Gson();

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		res.setStatus(302);
		res.setHeader("Location", "BeaconSetting.html");
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		res.setCharacterEncoding("UTF-8");
		System.out.println("BeaconSetting Request: " + req.getParameter("json"));
		Map<String, Object> map = gson.fromJson(req.getParameter("json"), Map.class);
		switch ((String) map.get("TYPE")) {
		case "GET_CLASS_ROOM":
			res.getWriter().print(gson.toJson(BeaconDAO.getInstance().getClassRooms()));
			break;
		case "CREATE":
			String classRoom = (String) map.get("CLASS_ROOM");
			String beaconType = (String) map.get("BEACON_TYPE");
			String hwid = (String) map.get("HWID");
			BeaconDAO.getInstance().createBeacon(classRoom, hwid, beaconType);
			res.getWriter().print("ok");
			break;
		case "GET_BEACON":
			List<Map<String, Object>> list = BeaconDAO.getInstance()
					.getBeaconByClassRoom((String) map.get("CLASS_ROOM")).stream().map(beacon -> {
						Map<String, Object> data = new HashMap<String, Object>();
						data.put("ID", beacon.getId());
						data.put("CLASS_ROOM", beacon.getClassRoom());
						data.put("TYPE", beacon.getType());
						data.put("HWID", beacon.getHwid());
						return data;
					}).collect(Collectors.toList());
			res.getWriter().print(gson.toJson(list));
			break;
		case "DELETE_BEACON":
			int id = (int) (double) map.get("ID");
			BeaconDAO.getInstance().deleteBeacon(id);
			res.getWriter().print("ok");
			break;
		default:
			res.sendError(404);
			break;
		}
	}

}
