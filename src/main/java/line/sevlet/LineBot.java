package line.sevlet;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.io.IOException;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.lang.reflect.Method;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

import line.LineEvent;
import line.api.event.BaseReplyEvent;
import line.api.event.EventAbstract;
import line.util.Util;

public class LineBot extends HttpServlet {

	private static final long serialVersionUID = 1L;
	public static final String LIFF_ID = "1611395821-zd5EGyoZ";
	private static final String LINE_API_URL = "https://api.line.me/v2/bot";
	private static final Gson gson = new Gson();
	private static final String channelSecret = "9ef254b41a42e547e0294fd2accae9e4";
	private static final String channelAccessToken = "tEiKGNqO3jaKqD1Jom0rAodKJB3M7FRr7RntWR1w6X8x9b7Il7ym7bVPtuYAPGOnD8BerCcpgjfGwWD1MCFWAU9wRkLLHQaYRl87c/y48F1CQLUrINtVf2F/U67qiaVRwAMrmRegmYe2mWVplcmP9wdB04t89/1O/w1cDnyilFU=";

	public static HttpURLConnection getHttpURLConnection(String path) throws Exception {
		URL url = new URL(LINE_API_URL + path);
		HttpURLConnection con = (HttpURLConnection) url.openConnection();
		con.addRequestProperty("Authorization", "Bearer " + channelAccessToken);
		return con;
	}

	private List<Class<?>> classas = new ArrayList<Class<?>>();

	public void addListener(Class<?> classas) {
		this.classas.add(classas);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		String signature = req.getHeader("X-Line-Signature");
		String request;
		try {
			request = Util.InputStreamToString(req.getInputStream());
			Mac mac = Mac.getInstance("HmacSHA256");
			SecretKeySpec secretKeySpec = new SecretKeySpec(channelSecret.getBytes(), "HmacSHA256");
			mac.init(secretKeySpec);
			String hash = Base64.getEncoder().encodeToString(mac.doFinal(request.getBytes()));
			if (!hash.equals(signature)) {
				res.setStatus(400);
				return;
			}
			res.setStatus(200);
		} catch (Exception e) {
			e.printStackTrace();
			res.setStatus(400);
			return;
		}
		List<Map<String, Object>> events = (List<Map<String, Object>>) gson.fromJson(request, HashMap.class)
				.get("events");
		events.forEach(map -> {
			EventAbstract event;
			try {
				event = EventAbstract.parseEvent(map);
				if (event instanceof BaseReplyEvent && ((BaseReplyEvent) event).isInvalid()) {
					return;
				}
				for (Class<?> classas : this.classas) {
					for (Method method : classas.getDeclaredMethods()) {
						if (method.isAnnotationPresent(EventHandle.class) && method.getParameterTypes().length == 1
								&& method.getParameterTypes()[0].equals(event.getClass())) {
							method.invoke(LineEvent.class.getDeclaringClass(), event);
						}
					}
				}
			} catch (Exception e2) {
				System.out.println(request);
				e2.printStackTrace();
			}
		});

	}

	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		res.setStatus(302);
		res.setHeader("Location", "/");
	}

	@Retention(RUNTIME)
	@Target(METHOD)
	public static @interface EventHandle {

	}
}
