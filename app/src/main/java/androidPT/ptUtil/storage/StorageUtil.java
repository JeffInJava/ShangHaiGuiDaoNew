package androidPT.ptUtil.storage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.alibaba.fastjson.JSON;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ineasnet.shanghaiguidao2.entity.bean.QueueBean;
import com.ineasnet.shanghaiguidao2.entity.vo.CacheVo;
import com.ineasnet.shanghaiguidao2.entity.vo.DepartmentVo;
import com.ineasnet.shanghaiguidao2.entity.vo.LoginVo;
import com.ineasnet.shanghaiguidao2.entity.vo.QueueVo;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.text.TextUtils;

public class StorageUtil {

	private SharedPreferences sp;
	private String result;
	private Context mContext;

	public StorageUtil(Context context) {
		// TODO Auto-generated constructor stub
		this.mContext = context;
	}

	public void storeLoginInfo(String userName, String password, String userId,
			String sessionId, boolean isAutoLogin) {
		Gson gson = new Gson();
		sp = mContext.getSharedPreferences("user_info", Activity.MODE_PRIVATE);
		String userListStr = sp.getString("userList", "");
		List<LoginVo> list  = new ArrayList<LoginVo>();
		LoginVo loginVo = null;
		if (TextUtils.isEmpty(userListStr)) {
			list = new ArrayList<LoginVo>();
			loginVo = new LoginVo();
			loginVo.setUserName(userName);
			loginVo.setPassword(password);
			loginVo.setUserId(userId);
			loginVo.setSessionId(sessionId);

			list.add(loginVo);
		} else {
			list = gson.fromJson(userListStr,new TypeToken<List<LoginVo>>(){}.getType());
//			list = JSON.parseArray(userListStr, LoginVo.class);
			if (userListStr.contains(userName)) {

				for (LoginVo vo : list) {
					if (vo.getUserName().equals(userName)) {
						loginVo = vo;
						break;
					}
				}
				loginVo.setPassword(password);

			} else {
				loginVo = new LoginVo();
				loginVo.setUserName(userName);
				loginVo.setPassword(password);
				loginVo.setUserId(userId);
				loginVo.setSessionId(sessionId);

				list.add(loginVo);
			}

		}

		userListStr = gson.toJson(list);
//		userListStr = JSON.toJSONString(list);

		Editor editor = sp.edit();
		editor.putString("userList", userListStr);
		editor.putString("lastUser", userName);
		editor.putBoolean("isAutoLogin", isAutoLogin);
		editor.commit();
		editor.clear();
	}
	public void storeAutoLoginChange(){
		sp = mContext.getSharedPreferences("user_info", Activity.MODE_PRIVATE);
		Editor editor = sp.edit();
		editor.putBoolean("isAutoLogin", false);
		editor.commit();
		editor.clear();
	}

	public String getUserInfoList() {
		sp = mContext.getSharedPreferences("user_info", Activity.MODE_PRIVATE);
		result = sp.getString("userList", "");
		return result;
	}

	public String getSessionId(String userId) {
		sp = mContext.getSharedPreferences("user_info", Activity.MODE_PRIVATE);
		result = "";
		try {
			String userListStr = sp.getString("userList", "");
			Gson gson = new Gson();

			LoginVo vo = gson.fromJson(userListStr,LoginVo.class);
			result = vo.getSessionId();
//			List<LoginVo> list = JSON.parseArray(userListStr, LoginVo.class);
//			for (LoginVo vo : list) {
//				if (vo.getUserId().equals(userId)) {
//					result = vo.getSessionId();
//				}
//			}

		} catch (Exception e) {
			// TODO: handle exception

		}
		return result;
	}

	public String getLastUser() {
		sp = mContext.getSharedPreferences("user_info", Activity.MODE_PRIVATE);
		result = sp.getString("lastUser", "");
		return result;
	}

	public boolean isAutoLogin() {
		sp = mContext.getSharedPreferences("user_info", Activity.MODE_PRIVATE);
		boolean isAutoLogin = sp.getBoolean("isAutoLogin", false);
		return isAutoLogin;
	}

	public void storeFileList(String userId, String listStr) {
		sp = mContext.getSharedPreferences(userId, Activity.MODE_PRIVATE);
		Editor editor = sp.edit();
		editor.putString("fileList", listStr);
		editor.commit();
		editor.clear();
	}

	public String getFileList(String userId) {
		sp = mContext.getSharedPreferences(userId, Activity.MODE_PRIVATE);
		result = sp.getString("fileList", "");
		return result;
	}

	public void storeCacheList(String userId, String key, String value) {
		sp = mContext.getSharedPreferences(userId, Activity.MODE_PRIVATE);
		String cacheListStr = sp.getString("cache", "");
		List<CacheVo> list = null;
		if (TextUtils.isEmpty(cacheListStr)) {
			CacheVo vo = new CacheVo();
			vo.setKey(key);
			vo.setValue(value);

			list = new ArrayList<>();
			list.add(vo);

		} else {
			list = JSON.parseArray(cacheListStr, CacheVo.class);
			if (cacheListStr.contains(key)) {
				int i = 0;
				for (CacheVo vo : list) {
					if (vo.getKey().equals(key)) {
						vo.setValue(value);
						list.set(i, vo);
					}
					i++;
				}
			} else {
				CacheVo vo = new CacheVo();
				vo.setKey(key);
				vo.setValue(value);

				list = new ArrayList<>();
				list.add(vo);
			}

		}

		result = JSON.toJSONString(list);

		Editor editor = sp.edit();
		editor.putString("cache", result);
		editor.commit();
		editor.clear();
	}

	public String getCacheValue(String userId, String key) {
		sp = mContext.getSharedPreferences(userId, Activity.MODE_PRIVATE);
		String cacheListStr = sp.getString("cache", "");
		if (TextUtils.isEmpty(cacheListStr) && !cacheListStr.contains(key)) {
			result = "";
		} else {
			List<CacheVo> list = JSON.parseArray(cacheListStr, CacheVo.class);
			for (CacheVo vo : list) {
				if (vo.getKey().equals(key)) {
					result = vo.getValue();
				}
			}
		}

		return result;
	}

	public String generateKey(String url, String key, String value) {
		JSONObject object = new JSONObject();
		try {
			object.put(key, value);
			result = url + object.toString();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}

	public void storeProIndex(String userId, String value) {
		sp = mContext.getSharedPreferences(userId, Activity.MODE_PRIVATE);
		Editor editor = sp.edit();
		editor.putString("proIndex", value);
		editor.commit();
		editor.clear();
	}

	public String getProIndex(String userId) {
		sp = mContext.getSharedPreferences(userId, Activity.MODE_PRIVATE);
		result = sp.getString("proIndex", "");
		return result;
	}
	public void storeStandardIndex(String userId, String value) {
		sp = mContext.getSharedPreferences(userId, Activity.MODE_PRIVATE);
		Editor editor = sp.edit();
		editor.putString("standardIndex", value);
		editor.commit();
		editor.clear();
	}

	public String getStandardIndex(String userId) {
		sp = mContext.getSharedPreferences(userId, Activity.MODE_PRIVATE);
		result = sp.getString("standardIndex", "");
		return result;
	}

	public void storeToken(String token) {
		sp = mContext.getSharedPreferences("user_info", Activity.MODE_PRIVATE);
		Editor editor = sp.edit();
		editor.putString("token", token);
		editor.commit();
		editor.clear();
	}

	public String getToken() {
		sp = mContext.getSharedPreferences("user_info", Activity.MODE_PRIVATE);
		result = sp.getString("token", "");
		return result;
	}

	public void storeDepartments(String userId, String depStr) {
		sp = mContext.getSharedPreferences(userId, Activity.MODE_PRIVATE);
		Editor editor = sp.edit();
		editor.putString("departments", depStr);
		editor.commit();
		editor.clear();
	}

	public String getDepList(String userId) {
		sp = mContext.getSharedPreferences(userId, Activity.MODE_PRIVATE);
		result = sp.getString("departments", "");

		return result;
	}

	public void storeQueueValue(String userId , String key, String value) {
		sp = mContext.getSharedPreferences(userId, Activity.MODE_PRIVATE);
		String queue = sp.getString("queue", "");
		if (TextUtils.isEmpty(queue)) {

			QueueVo queueVo = new QueueVo();
			queueVo.setKey(key + value);
			queueVo.setUrl(key);
			queueVo.setParam(value);
			queueVo.setState(0);
			JSONArray array = new JSONArray();
			JSONObject object = new JSONObject();
			JSONArray array2 = new JSONArray();

			array2.put(queueVo);

			try {
				object.put("type", key);
				object.put("list", array2);
			} catch (JSONException e) {
				e.printStackTrace();
			}
			array.put(object);
			Gson gson = new Gson();
//			array.add(object);
			result = JSON.toJSONString(array);

			Editor editor = sp.edit();
			editor.putString("queue", result);
			editor.commit();
			editor.clear();
		} else {
			List<QueueBean> list = JSON.parseArray(queue, QueueBean.class);
			if (queue.contains(key)) {
				for (QueueBean vo : list) {
					if (vo.getType().equals(key)) {
						QueueVo queueVo = new QueueVo();
						queueVo.setKey(key + value);
						queueVo.setUrl(key);
						queueVo.setParam(value);
						queueVo.setState(0);
						vo.getList().add(queueVo);
					}
				}
				result = JSON.toJSONString(list);

				Editor editor = sp.edit();
				editor.putString("queue", result);
				editor.commit();
				editor.clear();

			} else {
				QueueVo queueVo = new QueueVo();
				queueVo.setKey(key + value);
				queueVo.setUrl(key);
				queueVo.setParam(value);
				queueVo.setState(0);

				List<QueueVo> listVo = new ArrayList<QueueVo>();
				listVo.add(queueVo);

				QueueBean beanVo = new QueueBean();
				beanVo.setType(key);
				beanVo.setList(listVo);

				list.add(beanVo);

				result = JSON.toJSONString(list);
				Editor editor = sp.edit();
				editor.putString("queue", result);
				editor.commit();
				editor.clear();

			}
		}
	}

}
