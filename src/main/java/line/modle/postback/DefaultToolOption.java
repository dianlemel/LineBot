package line.modle.postback;

import java.util.HashMap;
import java.util.Map;

import line.api.action.PostbackAction;
import line.api.send.message.Text;
import line.modle.ModleAbstract;
import line.sql.data.User;
import line.sql.data.User.UserType;

public class DefaultToolOption extends ModleAbstract {

	public static final String KEY = "DEFAULT_TOOL_OPTION";

	public DefaultToolOption() {
		super(KEY);
	}

	@Override
	public void receive(User user, Map<String, Object> data, line.api.event.PostBackEvent event) {
		String type = (String) data.get("DATA");
		switch (type) {
		case "SEARCH":
			search(user, data, event);
			break;
		case "SETTING":
			setting(user, data, event);
			break;
		case "ACCOUNT_LINK":
			account_link(user, data, event);
		default:
			break;
		}
	}
	
	private void account_link(User user, Map<String, Object> data, line.api.event.PostBackEvent event) {
		user.getData().put("ACCOUNT_LINK", true);
		Text text = new Text("請輸入校園帳號");
		Map<String, Object> data2 = new HashMap<String, Object>();
		data2.put("TYPE", "ACCOUNT_LINK");
		data2.put("DATA", "CANCEL");
		text.addAction(new PostbackAction("取消", null, data2, null));
		event.reply(text, true);		
	}

	private void search(User user, Map<String, Object> data, line.api.event.PostBackEvent event) {
		Text text = new Text("請選擇要查詢的功能");
		Map<String, Object> data2 = new HashMap<String, Object>();
		data2.put("TYPE", "SEARCH");
		data2.put("DATA", "PERSONAL_INFOMATION");
		text.addAction(new PostbackAction("個人資訊", null, data2, null));
		data2 = new HashMap<String, Object>();
		data2.put("TYPE", "SEARCH");
		data2.put("DATA", "COURSE_LIST");
		text.addAction(new PostbackAction("所有課程", null, data2, null));
		if(user.getType().equals(UserType.TEACHER)) {
			data2 = new HashMap<String, Object>();
			data2.put("TYPE", "SEARCH");
			data2.put("DATA", "COURSE_NOW");
			text.addAction(new PostbackAction("正在進行中的課程", null, data2, null));			
		}
		event.reply(text, true);
	}

	private void setting(User user, Map<String, Object> data, line.api.event.PostBackEvent event) {
		Text text = new Text("請選擇要設定的功能");
		Map<String, Object> data2 = new HashMap<String, Object>();
		data2.put("TYPE", "SETTING");
		data2.put("DATA", "NAME");
		text.addAction(new PostbackAction("個人名稱", null, data2, null));
		data2 = new HashMap<String, Object>();
		data2.put("TYPE", "SETTING");
		data2.put("DATA", "INFOMATION");
		text.addAction(new PostbackAction("個人敘述", null, data2, null));
		event.reply(text, true);
	}

}
