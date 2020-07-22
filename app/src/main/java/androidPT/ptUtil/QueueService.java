package androidPT.ptUtil;//package androidPT.ptUtil;
//
//import java.util.Iterator;
//import java.util.List;
//
//import org.apache.commons.httpclient.NameValuePair;
//import org.json.JSONException;
//import org.json.JSONObject;
//
//import com.alibaba.fastjson.JSON;
//import com.ineasnet.shanghaiguidao.entity.bean.QueueBean;
//import com.ineasnet.shanghaiguidao.entity.vo.QueueVo;
//
//import android.app.Activity;
//import android.app.Service;
//import android.content.Intent;
//import android.content.SharedPreferences;
//import android.content.SharedPreferences.Editor;
//import android.os.IBinder;
//import android.text.TextUtils;
//import android.util.Log;
//
//public class QueueService extends Service {
//	private SharedPreferences sp;
//	private String queueStr;
//	private MyApplication myApp;
//	private String mUserId;
//	private String url ;
//	private String params ;
//	private NameValuePair[] pairs;
//	int position = 0 ;
//	
//
//	@Override
//	public IBinder onBind(Intent intent) {
//		// TODO Auto-generated method stub
//		return null;
//	}
//
//	@Override
//	public void onCreate() {
//		// TODO Auto-generated method stub
//		super.onCreate();
//		myApp = (MyApplication)getApplication();
//		mUserId = myApp.getUserId();
//		
//		sp = getSharedPreferences(mUserId, Activity.MODE_PRIVATE);
//		
//		queueStr = sp.getString("queue", "");
//		if(!TextUtils.isEmpty(queueStr)){
//			List<QueueBean> beans = JSON.parseArray(queueStr, QueueBean.class);
//			for(QueueBean bean : beans){
//				List<QueueVo> vos = bean.getList();
//				String type = bean.getType();
//				position = 0 ;
//				for(QueueVo vo : vos){
//					 url = vo.getUrl();
//					 params = vo.getParam();
//					
//					JSONObject object;
//					try {
//						object = new JSONObject(params);
//						Iterator it = object.keys();
//						pairs = new NameValuePair[object.length()];
//						int j = 0;
//						while(it.hasNext()){
//							String key = it.next().toString();
//							pairs[j] = new NameValuePair(key, object.getString(key));
//							j++;
//						}
//						new Thread(new Runnable() {
//							
//							@Override
//							public void run() {
//								// TODO Auto-generated method stub
//								HttpClientInstance instance = HttpClientInstance.getInstance();
//								instance.getPostResultWithCallBack(url, pairs,position, cb);
//							}
//						}).start();
//						
//					} catch (JSONException e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					}
//					
//					position++;
//				}
//			}
//			
//		}
//		
//		
//		
//	}
//	QueueCallBack cb = new QueueCallBack() {
//
//		@Override
//		public void onQueueReturnRes(String url, int position, String result) {
//			// TODO Auto-generated method stub
//			Log.e("queue call back result", "result:"+result);
//			queueStr = sp.getString("queue", "");
//			List<QueueBean> beans = JSON.parseArray(queueStr, QueueBean.class);
//			int location = 0;
//			for(QueueBean bean : beans){
//				if(bean.getType().equals(url)){
//					bean.getList().remove(position-1);
//					beans.set(location, bean);
//				}
//				location++;
//			}
//			String finalStr = JSON.toJSONString(beans);
//			Editor editor = sp.edit();
//			editor.putString("queue", finalStr);
//			editor.commit();
//			editor.clear();
//			
//		}
//		
//	};
//
//	@Override
//	public void onDestroy() {
//		// TODO Auto-generated method stub
//		super.onDestroy();
//	}
//
//	@Override
//	public int onStartCommand(Intent intent, int flags, int startId) {
//		return super.onStartCommand(intent, flags, startId);
//	}
//}
