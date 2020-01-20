package line.modle.postback;

import java.util.Map;

import line.api.send.message.Text;
import line.modle.ModleAbstract;
import line.sql.column.UserColumn;
import line.sql.dao.Update;
import line.sql.dao.UserDAO;
import line.sql.data.User;

public class SettingName extends ModleAbstract {

	public static final String KEY = "SETTING_NAME";

	public SettingName() {
		super(KEY);
	}

	@Override
	public void receive(User user, Map<String, Object> data, line.api.event.PostBackEvent event) {
		if (user.getData().containsKey("SETTING")) {
			switch ((String) data.get("DATA")) {
			case "CONFIRM":
				String name = (String) data.get("DATA_2");
				UserDAO.getInstance().updateUser(user, new Update(UserColumn.Name, name));
				event.reply(new Text("個人名稱已設定為: " + name), true);
				break;
			case "CANCEL":
				event.reply(new Text("已取消設定"), true);
				break;
			}
			user.getData().remove("SETTING");
		}
	}

}
