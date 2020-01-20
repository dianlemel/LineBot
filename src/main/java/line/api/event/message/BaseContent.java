package line.api.event.message;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Map;
import java.util.Optional;

public abstract class BaseContent extends MessageAbstract {

	private static final String ContentProvider = "type";
	private static final String ContentProvider_Type = "contentProvider";
	private static final String OriginalContentUrl = "originalContentUrl";
	private static final String PreviewImageUrl = "previewImageUrl";

	private final ContentProviderType contentProvider;
	private final String originalContentUrl;
	private final String previewImageUrl;

	@SuppressWarnings("unchecked")
	public BaseContent(Map<String, Object> map) {
		super(map);
		Map<String, Object> contentProviderMap = (Map<String, Object>) map.get(ContentProvider);
		contentProvider = ContentProviderType.valueOf((String) contentProviderMap.get(ContentProvider_Type));
		originalContentUrl = Optional.ofNullable(map.get(OriginalContentUrl)).map(String.class::cast).orElse("");
		previewImageUrl = Optional.ofNullable(map.get(PreviewImageUrl)).map(String.class::cast).orElse("");
	}

	public void saveContent(String path) throws Exception {
		if (contentProvider.equals(ContentProviderType.line)) {
			saveURL(path, "/message/" + getId() + "/content");
		} else {
			saveURL(originalContentUrl, path);
		}
	}

	public void savePreview(String path) throws Exception {
		if (contentProvider.equals(ContentProviderType.external)) {
			saveURL(previewImageUrl, path);
		}
	}

	private void saveURL(String url, String path) throws Exception {
		if (path == null || path.isEmpty()) {
			return;
		}
		HttpURLConnection con = null;
		try {
			Path p = Paths.get(path);
			con = (HttpURLConnection) new URL(url).openConnection();
			try (InputStream is = con.getInputStream()) {
				Files.copy(is, p, StandardCopyOption.REPLACE_EXISTING);
			}
		} finally {
			if (con != null) {
				con.disconnect();
			}
		}
	}

	public static enum ContentProviderType {
		line, external
	}

}
