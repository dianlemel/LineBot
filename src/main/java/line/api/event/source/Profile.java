package line.api.event.source;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Map;

public class Profile {
	
	private static final String USER_ID = "userId";
	private static final String DISPLAY_NAME = "displayName";
	private static final String PICTURE_URL = "pictureUrl";

	private final String user;
	private final String displayName;
	private final String pictureUrl;

	public Profile(Map<String, ?> map) {
		user = (String) map.get(USER_ID);
		displayName = (String) map.get(DISPLAY_NAME);
		pictureUrl = (String) map.get(PICTURE_URL);
	}

	public String getUser() {
		return user;
	}

	public String getDisplayName() {
		return displayName;
	}

	public String getPictureUrl() {
		return pictureUrl;
	}

	public void savePicture(String path) throws Exception {

		HttpURLConnection con = null;
		try {
			Path p = Paths.get(path);
			con = (HttpURLConnection) new URL(pictureUrl).openConnection();
			try (InputStream is = con.getInputStream()) {
				Files.copy(is, p, StandardCopyOption.REPLACE_EXISTING);
			}
		} finally {
			if (con != null) {
				con.disconnect();
			}
		}
	}

}
