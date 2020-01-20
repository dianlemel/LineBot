package line.api.event.message;

import java.util.Map;

public class File extends BaseContent {

	private static final String FileName = "fileName";
	private static final String FileSize = "fileSize";

	private final String fileName;
	private final double fileSize;

	public File(Map<String, Object> map) {
		super(map);
		fileName = (String) map.get(FileName);
		fileSize = (double) map.get(FileSize);
	}

	@Override
	public MessageType getType() {
		return MessageType.file;
	}

	public String getFileName() {
		return fileName;
	}

	public double getFileSize() {
		return fileSize;
	}

}
