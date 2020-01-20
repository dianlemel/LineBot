package line.modle.postback;

import java.util.Map;

import line.api.event.PostBackEvent;
import line.api.send.message.Text;
import line.modle.ModleAbstract;
import line.sql.column.UserColumn;
import line.sql.dao.Update;
import line.sql.dao.UserDAO;
import line.sql.data.User;
import line.sql.data.User.UserType;

public class AccountLink extends ModleAbstract {

	public AccountLink() {
		super("ACCOUNT_LINK");
	}

	@Override
	public void receive(User user, Map<String, Object> data, PostBackEvent event) {
		if (!user.getData().containsKey("ACCOUNT_LINK")) {
			return;
		}
		Text text;
		switch ((String) data.get("DATA")) {
		case "CANCEL":
			text = new Text("已取消綁定帳號");
			event.reply(text, true);
			user.getData().remove("ACCOUNT_LINK");
			break;
		case "CONFIRM":
			String account = (String) data.get("DATA_2");
			if (UserDAO.getInstance().setUserAccount(user, account)) {
				UserDAO.getInstance().updateUser(user, new Update(UserColumn.Type, UserType.STUDENT));
				user.setType(UserType.STUDENT);
				text = new Text("成功綁定帳號");
			} else {
				text = new Text("綁定帳號失敗");
			}
			event.reply(text, true);
			user.getData().remove("ACCOUNT_LINK");
			break;
		default:
			break;
		}
	}

}
