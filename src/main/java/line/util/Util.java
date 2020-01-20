package line.util;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.CharBuffer;

import javax.imageio.ImageIO;

import com.swetake.util.Qrcode;

import line.sql.data.Course;

public class Util {

	public static String InputStreamToString(InputStream is) throws Exception {
		CharBuffer charBuffer = CharBuffer.allocate(10);
		StringBuilder stringBuilder = new StringBuilder();
		try (BufferedReader bufferReader = new BufferedReader(new InputStreamReader(is))) {
			while (bufferReader.ready()) {
				charBuffer.clear();
				bufferReader.read(charBuffer);
				stringBuilder.append(charBuffer.flip());
			}
		}
		return stringBuilder.toString();
	}

	public static void generateQRCode(Course course) {
		try {
			Qrcode qrcode = new Qrcode();
			qrcode.setQrcodeErrorCorrect('M');
			qrcode.setQrcodeEncodeMode('B');
			qrcode.setQrcodeVersion(7);
			byte[] contentBytes = course.getInviteCode().getBytes("gb2312");
			BufferedImage bufImg = new BufferedImage(140, 140, BufferedImage.TYPE_INT_RGB);
			Graphics2D gs = bufImg.createGraphics();
			gs.setBackground(Color.WHITE);
			gs.clearRect(0, 0, 140, 140);
			gs.setColor(Color.BLACK);
			int pixoff = 2;
			if (contentBytes.length > 0 && contentBytes.length < 120) {
				boolean[][] codeOut = qrcode.calQrcode(contentBytes);
				for (int i = 0; i < codeOut.length; i++) {
					for (int j = 0; j < codeOut.length; j++) {
						if (codeOut[j][i]) {
							gs.fillRect(j * 3 + pixoff, i * 3 + pixoff, 3, 3);
						}
					}
				}
			} else {
				System.err.println("QRCode content bytes length = " + contentBytes.length + " not in [ 0,120 ]. ");
			}
			gs.dispose();
			bufImg.flush();
			File imgFile = new File(
					System.getProperty("user.dir") + "/WebContent/qrcode/" + course.getInviteCode() + ".jpg");
			ImageIO.write(bufImg, "png", imgFile);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
