package androidPT.ptUtil.internet;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.security.KeyStore;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import com.google.gson.Gson;
import com.ineasnet.shanghaiguidao2.activity.StartActivity;
import com.ineasnet.shanghaiguidao2.entity.bean.BitmapBean;

import android.content.Context;
import android.util.Log;

import androidPT.ptUtil.PropertiesUtil;
import androidPT.ptUtil.internet.HttpResultCallBack;

public class HttpInstance {
	// private static String baseUrl;
//	 public static String baseUrl = "http://10.20.66.81:8080/paperless/";// 赵
    //public static String baseUrl = "http://172.23.31.7:8080/paperless/";//运管
    public static String baseUrl = "http://172.20.31.7:8080/paperless/";//运管外网
    //public static String baseUrl = "http://10.1.49.26:8084/paperless/";//二运
	//public static String baseUrl = "http://10.20.1.50:8080/paperless/";//运管
//	public static String baseUrl = "https://192.168.43.117:8081/paperless/";// 本机测试
	private static HttpInstance mInstance;
	private static HttpClient mClient;
	private String result = "";
	private int statusCode = 0;

	public static String ACTION_READ = "com.inseanet.shanghaiguidao.read";
	public static String ACTION_UNREAD = "com.inseanet.shanghaiguidao.u+nread";
	public static String ACTION_BACK = "com.inseanet.shanghaiguidao.back";
	public static String ACTION_NONET = "com.inseanet.shanghaiguidao.nonet";
	public static String ACTION_ADDDOWNLOAD = "com.inseanet.shanghaiguidao.add";
	public static String ACTION_DELETEDOWNLOAD = "com.inseanet.shanghaiguidao.delete";
	public static String ACTION_UPDATEDOWNLOAD = "com.inseanet.shanghaiguidao.update";
	public static String ACTION_DOWNLOADFINISH = "com.inseanet.shanghaiguidao.finish";

	public HttpInstance() {
		// TODO Auto-generated constructor stub
	}

	public static HttpInstance getHttpInstance() {
		if (mInstance == null) {
			mInstance = new HttpInstance();

		}
		return mInstance;
	}

	public static synchronized HttpClient getHttpClient() {
		if (mClient == null) {
			mClient = getHttpsClient();
			mClient.getParams().setParameter(
					CoreConnectionPNames.CONNECTION_TIMEOUT, 60*1000);
			mClient.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT,
					60*1000);
		}
		return mClient;
	}

	public String getHttpPostResult(String url, List<NameValuePair> params) {
		Log.e("ip", "baseurl:" + baseUrl);
		HttpResponse response = null;
		HttpPost post = new HttpPost(baseUrl + url + "token="
				+ StartActivity.token);
		mClient = getHttpClient();
		try {
			if (params != null) {
				HttpEntity entity = new UrlEncodedFormEntity(params);
				post.setEntity(entity);
			}
			response = mClient.execute(post);
			statusCode = response.getStatusLine().getStatusCode();
			if (statusCode == 200) {
				result = EntityUtils.toString(response.getEntity());
			}

			// cb.onResultCallBack(statusCode, result);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block

			e.printStackTrace();
			return "";
		}

		return result;

	}

	public String uploadSign(String url, List<String> filePath,List<String> users ,String tblStudyDataId,String userId,String time) {
		mClient = getHttpClient();
		HttpPost httpPost = new HttpPost(baseUrl + url + "tblStudyDataId="
				+ tblStudyDataId+"&userId="+userId+"&studyTime="+time);

		File[] files = null;
		Gson gson = new Gson();
		MultipartEntityBuilder builder = MultipartEntityBuilder.create();
		builder.setCharset(Charset.forName(HTTP.UTF_8));// 设置请求的编码格式
		builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
		int count = 0;
		List<BitmapBean> bitmapBeen = new ArrayList<>();
		for (int i=0;i<filePath.size();i++){
			File file = new File(filePath.get(i));
			if ( file.exists()) {
				String fileName = file.getName();
				builder.addBinaryBody(fileName, file);

				String usersId = users.get(i);
				BitmapBean bean = new BitmapBean();
				bean.setUserId(usersId);
				bean.setFileName(fileName);
				bitmapBeen.add(bean);

			}
		}

		String json = gson.toJson(bitmapBeen);
		builder.addTextBody("users",json);
		builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
		// builder.addPart("PhotoMessage", fileBody);
		// HttpEntity entity = builder.build();

		httpPost.setEntity(builder.build());

		HttpResponse response;
		try {
			response = mClient.execute(httpPost);
			HttpEntity resEntity = response.getEntity();
			if (resEntity != null) {
				result = EntityUtils.toString(resEntity, "utf-8");
			}

		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return result;

	}

	/**
	 * https请求处理
	 * @return
	 */
	private static synchronized HttpClient getHttpsClient() {
		try {
			KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
			trustStore.load(null, null);
			SSLSocketFactory sf = new SSLSocketFactoryEx(trustStore);
			sf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);

			HttpParams params = new BasicHttpParams();
			HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
			HttpProtocolParams.setContentCharset(params, HTTP.UTF_8);

			SchemeRegistry registry = new SchemeRegistry();
			registry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
			registry.register(new Scheme("https", sf, 443));

			ClientConnectionManager ccm = new ThreadSafeClientConnManager(params, registry);

			return new DefaultHttpClient(ccm, params);
		} catch (Exception e) {
			return new DefaultHttpClient();
		}
	}
}
