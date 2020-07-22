package androidPT.ptUtil;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.zip.ZipException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;



import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import androidPT.ptUtil.internet.HttpInstance;
import androidPT.ptUtil.internet.HttpResultCallBack;

public class DownLoadTask extends AsyncTask<String, Integer, String> {
	private String downloadPath;
	private String fileName;
	private Context mContext;
	private HttpResultCallBack cb;
	private String sessionId;
	private Handler mHandler;
	private String url;
	private int mPosition;
	
	private String path , folder;

	public DownLoadTask(Context context, String path, String sessionId,int position,
			HttpResultCallBack cb, Handler handler) {
		// TODO Auto-generated constructor stub
		this.mContext = context;
		this.downloadPath = path;
		this.cb = cb;
		this.mHandler = handler;
		this.sessionId = sessionId;
		this.mPosition = position;
		// this.userId = userId;
		// this.mResult = result;
	}

	@Override
	protected String doInBackground(String... params) {
		// TODO Auto-generated method stub
		if(params.length==1){
			url = params[0];
		}else if(params.length == 3){
			url = params[0];
			path = params[1];
			folder = params[2];
		}
		download(url);

		return null;
	}

	@Override
	protected void onProgressUpdate(Integer... values) {
		// TODO Auto-generated method stub
		super.onProgressUpdate(values);
		// Log.e("progress", "progress:"+values[0]);
		Message msg = new Message();
		msg.what = 4;
		msg.obj = url;
		msg.arg1 = values[0];
		msg.arg2 = mPosition;
		mHandler.sendMessage(msg);
	}

	@Override
	protected void onPostExecute(String result) {
		// TODO Auto-generated method stub
		super.onPostExecute(result);
	}

	@Override
	protected void onCancelled() {
		// TODO Auto-generated method stub
		super.onCancelled();
	}

	// private void download(String url) {
	//
	// // if(httpClient==null){
	// httpClient = new org.apache.commons.httpclient.HttpClient();
	// // }
	// // httpClient = HttpClientInstance.getCurrentClient();
	// // sp = mContext.getSharedPreferences(userId, Activity.MODE_PRIVATE);
	// // sessionId = sp.getString("sessionId", "");
	// sessionId = MainActivity.sessionId;
	// GetMethod get = new GetMethod(url + "?_sessionid=" + sessionId);
	// // GetMethod get = new GetMethod(url);
	// try {
	// int statusCode = httpClient.executeMethod(get);
	// Log.e("download status", "status Code:" + statusCode);
	// if (statusCode == 200) {
	// InputStream is = get.getResponseBodyAsStream();
	// File file = new File(downloadPath);
	// if (!file.isDirectory()) {
	// Log.e("file directory", "1111111");
	// file.mkdirs();
	// }
	//
	// int index = url.lastIndexOf("/");
	// fileName = url.substring(index);
	// Log.e("fileName", "fileName:" + fileName);
	// int index2 = fileName.lastIndexOf(".");
	// String zipFolderName = fileName.substring(0, index2);
	// Log.e("folderName", "folderName:" + downloadPath
	// + zipFolderName + "/");
	// FileOutputStream fos = new FileOutputStream(downloadPath
	// + fileName);
	// byte buf[] = new byte[1024];
	// int length = -1;
	// int count = 0;
	// while ((length = is.read(buf, 0, 1024)) != -1) {
	// fos.write(buf, 0, length);
	// // publishProgress((int) ((count / (float) total) * 100));
	// }
	// fos.close();
	// is.close();
	// cb.onResultCallBack("success");
	// }
	// } catch (Exception e) {
	// // TODO: handle exception
	// Log.e("err", e.getMessage());
	// }
	// }

	private void download(String url) {
		try {
			HttpClient client = new DefaultHttpClient();
			// sessionId = MainActivity.sessionId;
			HttpGet get = new HttpGet(url + "?_sessionid=" + sessionId);
			HttpResponse response = client.execute(get);
			if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				HttpEntity entity = response.getEntity();
				InputStream is = entity.getContent();
				long total = entity.getContentLength();
				ByteArrayOutputStream baos = new ByteArrayOutputStream();

				File file = new File(downloadPath);
				if (!file.isDirectory()) {
					Log.e("file directory", "1111111");
					file.mkdirs();
				}

				int index = url.lastIndexOf("/") + 1;
				fileName = url.substring(index);

				FileOutputStream fos = new FileOutputStream(downloadPath
						+ fileName);

				byte[] buf = new byte[1024];
				int count = 0;
				int length = -1;
				while ((length = is.read(buf, 0, 1024)) != -1) {
					// baos.write(buf, 0, length);
					fos.write(buf, 0, length);
					count += length;
					// 调用publishProgress公布进度,最后onProgressUpdate方法将被执行
					publishProgress((int) ((count / (float) total) * 100));
					// // 为了演示进度,休眠500毫秒
					// Thread.sleep(500);
				}

				fos.close();
				is.close();

				if (fileName.contains(".zip")) {
					
					ZipTool zipTool = new ZipTool(mContext, "");
					try {
						zipTool.execute(path, folder);
					} catch (ZipException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}

				cb.onResultCallBack("success");
			}else{
				cb.onResultCallBack("failure");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
