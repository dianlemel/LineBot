package line.modle.postback;

import java.util.HashMap;
import java.util.Map;

import line.api.action.PostbackAction;
import line.api.send.message.Text;
import line.modle.ModleAbstract;
import line.sql.data.User;

public class Setting extends ModleAbstract {

	public static final String KEY = "SETTING";

	public Setting() {
		super(KEY);
	}

	@Override
	public void receive(User user, Map<String, Object> data, line.api.event.PostBackEvent event) {
		String type = (String) data.get("DATA");
		switch (type) {
		case "NAME":
			name(user, data, event);
			break;
		case "INFOMATION":
			infoamtion(user, data, event);
		default:
			break;
		}
	}
	
	private void infoamtion(User user, Map<String, Object> data, line.api.event.PostBackEvent event) {
		Text text = new Text("請輸入個人敘述，如果想要取消設定請點選取消按鈕。");
		Map<String, Object> data2 = new HashMap<String, Object>();
		data2.put("TYPE", "SETTING_NAME");
		data2.put("DATA", "CANCEL");
		text.addAction(new PostbackAction("取消", null, data2, null));
		event.reply(text, true);
		user.getData().put("SETTING", "SETTING_INFOMATION");		
	}

	private void name(User user, Map<String, Object> data, line.api.event.PostBackEvent event) {
		Text text = new Text("請輸入新的使用者名稱，如果想要取消設定請點選取消按鈕。");
		Map<String, Object> data2 = new HashMap<String, Object>();
		data2.put("TYPE", "SETTING_NAME");
		data2.put("DATA", "CANCEL");
		text.addAction(new PostbackAction("取消", null, data2, null));
		event.reply(text, true);
		user.getData().put("SETTING", "SETTING_NAME");
	}

}
