package androidPT.ptUtil.internet;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.alibaba.fastjson.JSON;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

public class NetCheckReceiver extends BroadcastReceiver {
	private static final String netACTION = "android.net.conn.CONNECTIVITY_CHANGE";
	private ConnectivityManager connectivityManager;
	private NetworkInfo info;
	private String result;
	@Override
	public void onReceive(final Context context, Intent intent) {
		// TODO Auto-generated method stub
		if (intent.getAction().equals(netACTION)) {
			Log.e("mark", "网络状态已经改变");
			connectivityManager = (ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			info = connectivityManager.getActiveNetworkInfo();
			if (info != null && info.isAvailable()) {
				String name = info.getTypeName();
				Log.e("mark", "当前网络名称：" + name);


			} else {
				Intent intent2 = new Intent(HttpInstance.ACTION_NONET);
				context.sendBroadcast(intent2);
				Log.e("mark", "没有可用网络");
			}
		}
	}
}
