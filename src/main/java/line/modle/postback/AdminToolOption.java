package line.modle.postback;

import java.util.Map;

import line.api.action.URIAction;
import line.api.event.PostBackEvent;
import line.api.send.message.Template;
import line.api.send.message.template.CarouselTemplate;
import line.api.send.message.template.carousel.Column;
import line.modle.ModleAbstract;
import line.sevlet.LineBot;
import line.sql.data.User;

public class AdminToolOption extends ModleAbstract {

	public AdminToolOption() {
		super("ADMIN_TOOL_OPTION");
	}

	@Override
	public void receive(User user, Map<String, Object> data, PostBackEvent event) {
		CarouselTemplate carouselTemplate = new CarouselTemplate();
		Column column = new Column(null, null, null, "請選擇功能", null);
		column.addAction(new URIAction("設定Beacon", "line://app/" + LineBot.LIFF_ID + "?Path=BeaconSetting", null));
		column.addAction(new URIAction("編輯使用者", "line://app/" + LineBot.LIFF_ID + "?Path=EditUser", null));
		carouselTemplate.addColumn(column);
		Template rtext = new Template("請在手機上查看該訊息", carouselTemplate);
		user.pushMessage(rtext, true);

	}

}
