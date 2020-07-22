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
//import android.content.Context;
//import android.content.SharedPreferences;
//
//public class QueueManager {
//
//	private Context mContext;
//	private String mUserId;
//	private SharedPreferences sp;
//	private List<QueueBean> queueList;
//
//	public QueueManager(Context context, String userId) {
//		// TODO Auto-generated constructor stub
//		this.mContext = context;
//		this.mUserId = userId;
//		sp = mContext.getSharedPreferences(userId, Activity.MODE_PRIVATE);
//	}
//
//	public void startPost() {
//		HttpClientInstance instance = HttpClientInstance.getInstance();
//		String queueStr = sp.getString("queue", "");
//		queueList = JSON.parseArray(queueStr, QueueBean.class);
//		for (QueueBean bean : queueList) {
//			String type = bean.getType();
//			List<QueueVo> voList = bean.getList();
//			for (QueueVo vo : voList) {
//				if (vo.getState() == 0) {
//					String url = vo.getUrl();
//					String dataStr = vo.getParam();
//					JSONObject object;
//					try {
//						object = new JSONObject(dataStr);
//						NameValuePair[] pairs = new NameValuePair[object
//								.length()];
//						Iterator it = object.keys();
//						int i = 0;
//						while (it.hasNext()) {
//							String key = it.next().toString();
//							String value = object.getString(key);
//
//							pairs[i] = new NameValuePair(key, value);
//							i++;
//
//						}
//						String result = instance.getHttpPostResult(url, pairs);
//
//						if (isSuccess(result)) {// 如果成功 status改为1 继续遍历请求
//							vo.setState(1);
//							continue;
//						} else {// 如果失败 status为0 继续遍历请求
//							vo.setState(0);
//							continue;
//						}
//
//					} catch (JSONException e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					}
//
//				}
//
//			}
//
//		}
//
//	}
//
//	private boolean isSuccess(String result) {
//		return true;
//	}
//
//}
