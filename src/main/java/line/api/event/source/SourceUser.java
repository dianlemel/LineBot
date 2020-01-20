package line.api.event.source;

import java.net.HttpURLConnection;
import java.util.Map;

import com.google.gson.Gson;

import line.sevlet.LineBot;
import line.util.Util;

public class SourceUser extends SourceAbstract {

	private static final Gson gson = new Gson();

	public SourceUser(Map<String, Object> map) {
		super(map);
	}

	@Override
	public SourceType getType() {
		return SourceType.user;
	}

	@SuppressWarnings("unchecked")
	public Profile getProfile() {
		try {
			HttpURLConnection con = LineBot.getHttpURLConnection("/profile/" + getUserId());
			con.setDoInput(true);
			return new Profile(gson.fromJson(Util.InputStreamToString(con.getInputStream()), Map.class));
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

}
