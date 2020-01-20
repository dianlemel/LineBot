package line.sevlet;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

import line.sql.column.UserColumn;
import line.sql.dao.Update;
import line.sql.dao.UserDAO;
import line.sql.data.User;
import line.sql.data.User.UserType;

public class EditUser extends HttpServlet {

	private static final long serialVersionUID = 1547614684330404434L;
	private static final Gson gson = new Gson();

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		res.setStatus(302);
		res.setHeader("Location", "EditUser.html");
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		res.setCharacterEncoding("UTF-8");
		System.out.println("EditUser Request: " + req.getParameter("json"));
		Map<String, Object> map = gson.fromJson(req.getParameter("json"), Map.class);
		switch ((String) map.get("TYPE")) {
		case "TYPE":
			type(map);
			break;
		case "MEMBER":
			res.getWriter().print(member(map));
			break;
		}
	}

	public String member(Map<String, Object> map) {
		List<Map<String, Object>> members = UserDAO.getInstance().getUsers().stream().map(user -> {
			Map<String, Object> member = new HashMap<>();
			member.put("ID", user.getId());
			member.put("NAME", user.getName());
			member.put("NAMENICK", user.getNickName());
			member.put("ACCOUNT", user.getAccount());
			member.put("TYPE", user.getType());
			return member;
		}).collect(Collectors.toList());
		return gson.toJson(members);
	}

	public void type(Map<String, Object> map) {
		User user = UserDAO.getInstance()
				.getUserByID(Optional.of(map.get("DATA")).map(Double.class::cast).map(Double::intValue).get());
		UserType type = Optional.of(map.get("DATA2")).map(String.class::cast).map(UserType::valueOf).get();
		UserDAO.getInstance().updateUser(user, new Update(UserColumn.Type, type));
		user.setType(type);
	}

}
