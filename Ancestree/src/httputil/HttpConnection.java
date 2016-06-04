package httputil;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.CookieManager;
import java.net.HttpCookie;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;

public class HttpConnection implements Runnable {

	public static final int DID_START = 0;
	public static final int DID_ERROR = 1;
	public static final int DID_SUCCEED = 2;

	private HttpURLConnection conn;
	private CallbackListener listener;
	private String params="";
	public static CookieManager cookie=null;

	// public HttpConnection() {
	// this(new Handler());
	// }

	public void create(String method, String urlString,
			Hashtable<String, String> data, CallbackListener listener) {
		if (data != null && data.size() > 0) {
			StringBuffer sb = new StringBuffer();
			if (method.equals("GET")) {
				sb.append("?");
			}
			int i = 0;
			Enumeration<String> keys = data.keys();
			while (keys.hasMoreElements()) {
				if (i++ != 0) {
					sb.append('&');
				}
				String key = keys.nextElement();
				try {
					sb.append(URLEncoder.encode(key, "utf8"));
					sb.append('=');
					sb.append(URLEncoder.encode(data.get(key), "utf8"));
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			params = sb.toString();
		}
		if (method.equals("GET")) {
			urlString += params;
		}
		try {
			URL url = new URL(urlString);
			conn = (HttpURLConnection) url.openConnection();
			conn.setConnectTimeout(15000);
			conn.setReadTimeout(5000);
			if(cookie!=null&&cookie.getCookieStore().getCookies().size() > 0)
			{
				conn.setRequestProperty("Cookie",
			    TextUtils.join(";",  cookie.getCookieStore().getCookies()));    
			}
			if (method.equals("POST")) {
				conn.setRequestMethod("POST");
				conn.setDoOutput(true);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.listener = listener;
		ConnectionManager.getInstance().push(this);
	}

	public void get(String url, Hashtable<String, String> data) {
		create("GET", StaticInfos.SERVERURL+url, data, listener);
	}

	public void post(String url, Hashtable<String, String> data,
			CallbackListener listener) {
		create("POST", StaticInfos.SERVERURL+url, data, listener);
	}

	public interface CallbackListener {
		public void callBack(String result);
	}

	private static final Handler handler = new Handler() {
		@Override
		public void handleMessage(Message message) {
			switch (message.what) {
			case HttpConnection.DID_START: {
				break;
			}
			case HttpConnection.DID_SUCCEED: {
				CallbackListener listener = (CallbackListener) message.obj;
				Object data = message.getData();
				if (listener != null) {
					if (data != null) {
						Bundle bundle = (Bundle) data;
						String result = bundle.getString("callbackkey");
						listener.callBack(result);
					}
				}
				break;
			}
			case HttpConnection.DID_ERROR: {
				break;
			}
			}
		}
	};

	public void run() {
		try {
			OutputStream out = conn.getOutputStream();
    		out.write(params.getBytes());
    		out.close();
			InputStream in = conn.getInputStream();
			
			List<String> cookiesHeader = conn.getHeaderFields().get("Set-Cookie");
			
			if(cookie==null){
				cookie=new CookieManager();
			}
			if(cookiesHeader != null)
			{
			    for (String c : cookiesHeader) 
			    {
			      cookie.getCookieStore().add(null,HttpCookie.parse(c).get(0));
			    }               
			}
			
			String result = null;
			if (!isHttpSuccessExecuted(conn)) {
				result = "fail";
			} else {
				result = getStringfromInputStream(in);
			}
			sendMessage(result);
			in.close();
			conn.disconnect();
		} catch (Exception ex) {
			sendMessage("error");
		}
		ConnectionManager.getInstance().didComplete(this);
	}

	private void sendMessage(String result) {
		Message message = Message.obtain(handler, DID_SUCCEED, listener);
		Bundle data = new Bundle();
		data.putString("callbackkey", result);
		message.setData(data);
		handler.sendMessage(message);

	}

	public static boolean isHttpSuccessExecuted(HttpURLConnection conn) {
		int statusCode = -1;
		try {
			statusCode = conn.getResponseCode();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return (statusCode > 199) && (statusCode < 400);
	}

	private static String getStringfromInputStream(InputStream inputStream) {
		// TODO Auto-generated method stub
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		byte[] data = new byte[1024];
		int len = 0;
		String result = "";
		if (inputStream != null) {
			try {
				while ((len = inputStream.read(data)) != -1) {
					outputStream.write(data, 0, len);
				}
				result = new String(outputStream.toByteArray(), "utf8");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return result;
	}

}
